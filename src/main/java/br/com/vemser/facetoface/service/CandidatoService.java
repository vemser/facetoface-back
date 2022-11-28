package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import br.com.vemser.facetoface.repository.ImageRepository;
import br.com.vemser.facetoface.repository.TrilhaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatoService {
    private final CandidatoRepository candidatoRepository;
    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final EdicaoService edicaoService;
    private final TrilhaService trilhaService;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO, Genero genero) throws RegraDeNegocioException, IOException {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByEmail(candidatoCreateDTO.getEmail());
        if(candidatoEntityOptional.isPresent()){
            throw new RegraDeNegocioException("Candidato com este e-mail já existe no sistema.");
        }
        for (LinguagemDTO linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem.getNome());
            linguagemList.add(byNome);
        }
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        candidatoEntity.setTrilha(trilhaService.findByNome(candidatoCreateDTO.getTrilha().getNome()));
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(linguagemList);
        candidatoEntity.setGenero(genero);
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
        candidatoRepository.save(candidatoEntity);
    }

    public CandidatoDTO update(Integer id, CandidatoCreateDTO candidatoCreateDTO, Genero genero) throws RegraDeNegocioException {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        findById(id);
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        for (LinguagemDTO linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem.getNome());
            linguagemList.add(byNome);
        }
        candidatoEntity.setIdCandidato(id);
        candidatoEntity.setTrilha(trilhaService.findByNome(candidatoCreateDTO.getTrilha().getNome()));
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(linguagemList);
        candidatoEntity.setGenero(genero);
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

    public CandidatoEntity findByEmailEntity(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if(candidatoEntity.isEmpty()){
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return candidatoEntity.get();
    }

    public PageDTO<CandidatoDTO> findByNomeCompleto(String nomeCompleto, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("nome_completo");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.findByNomeCompleto(nomeCompleto, pageRequest);
        if(candidatoEntityPage.isEmpty()){
            throw new RegraDeNegocioException("Candidato com o nome especificado não existe");
        }
        List<CandidatoDTO> candidatoDTOList = candidatoRepository.findAll()
                .stream()
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    private CandidatoEntity converterEntity(CandidatoCreateDTO candidatoCreateDTO) {
        return objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
    }

    public CandidatoDTO converterEmDTO(CandidatoEntity candidatoEntity) {
        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
        candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
        candidatoDTO.setTrilha(objectMapper.convertValue(candidatoEntity.getTrilha(), TrilhaDTO.class));
        candidatoDTO.setLinguagens(candidatoEntity.getLinguagens()
                .stream()
                .map(linguagem -> objectMapper.convertValue(linguagem, LinguagemDTO.class))
                .collect(Collectors.toList()));
        return candidatoDTO;
    }

    private CandidatoEntity findByNome(String nome) throws RegraDeNegocioException{
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByNomeCompleto(nome);
        return objectMapper.convertValue(candidatoEntityOptional, CandidatoEntity.class);
    }
}
