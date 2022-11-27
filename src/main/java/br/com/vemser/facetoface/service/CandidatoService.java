package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import br.com.vemser.facetoface.repository.TrilhaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidatoService {
    private final CandidatoRepository candidatoRepository;
    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final EdicaoService edicaoService;
    private final TrilhaService trilhaService;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException{
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        for (LinguagemDTO linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem.getNome());
            linguagemList.add(byNome);
        }
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        candidatoEntity.setTrilha(trilhaService.findByNome(candidatoCreateDTO.getTrilha().getNome()));
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(linguagemList);
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public PageDTO<CandidatoDTO> list(Integer pagina, Integer tamanho){
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.findAll(pageRequest);
        List<CandidatoDTO> candidatoDTOList = candidatoRepository.findAll().stream()
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);
        candidatoEntity.setAtivo('F');
    }

    public CandidatoDTO update(Integer id, CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        findById(id);
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        candidatoEntity.setIdCandidato(id);

        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public CandidatoEntity findById(Integer id) throws RegraDeNegocioException {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Candidato não encontrado."));
    }

    public CandidatoDTO findByEmail(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if(candidatoEntity.isEmpty()){
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return converterEmDTO(candidatoEntity.get());
    }

    public CandidatoDTO findByNomeCompleto(String nomeCompleto) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByNomeCompleto(nomeCompleto);
        if(candidatoEntity.isEmpty()){
            throw new RegraDeNegocioException("Candidato com o nome especificado não existe");
        }
        return converterEmDTO(candidatoEntity.get());
    }

    private CandidatoEntity converterEntity(CandidatoCreateDTO candidatoCreateDTO) {
        return objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
    }

    private CandidatoDTO converterEmDTO(CandidatoEntity candidatoEntity) {
        return objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
    }
}
