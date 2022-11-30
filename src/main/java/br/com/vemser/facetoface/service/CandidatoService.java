package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.*;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO, Genero genero) throws RegraDeNegocioException {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByEmail(candidatoCreateDTO.getEmail());
        if (candidatoEntityOptional.isPresent()) {
            throw new RegraDeNegocioException("Candidato com este e-mail já existe no sistema.");
        }
        for (LinguagemDTO linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem.getNome());
            linguagemList.add(byNome);
        }
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        candidatoEntity.setNomeCompleto(candidatoEntity.getNomeCompleto().trim());
        candidatoEntity.setTrilha(trilhaService.findByNome(candidatoCreateDTO.getTrilha().getNome()));
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        candidatoEntity.setGenero(genero);
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public PageDTO<CandidatoDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.findAll(pageRequest);
        List<CandidatoDTO> candidatoDTOList = candidatoEntityPage.stream()
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
        candidatoEntity.setNomeCompleto(candidatoEntity.getNomeCompleto().trim());
        candidatoEntity.setTrilha(trilhaService.findByNome(candidatoCreateDTO.getTrilha().getNome()));
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        candidatoEntity.setGenero(genero);
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public CandidatoEntity findById(Integer id) throws RegraDeNegocioException {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Candidato não encontrado."));
    }

    public CandidatoDTO findByEmail(String email) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findByEmailEntity(email);
        return converterEmDTO(candidatoEntity);
    }

    public CandidatoEntity findByEmailEntity(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if (candidatoEntity.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return candidatoEntity.get();
    }

    public PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage = candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto.trim(), nomeTrilha, pageRequest);
        if (candidatoEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
        }
        List<RelatorioCandidatoCadastroDTO> relatorioCandidatoCadastroDTOPage = candidatoEntityPage
                .stream()
                .map(x -> objectMapper.convertValue(x, RelatorioCandidatoCadastroDTO.class))
                .toList();
        for(RelatorioCandidatoCadastroDTO candidato: relatorioCandidatoCadastroDTOPage){
            CandidatoEntity candidatoEntity = findByEmailEntity(candidato.getEmail());
            List<String> linguagemList = candidatoEntity.getLinguagens()
                    .stream()
                    .map(LinguagemEntity::getNome)
                    .toList();
            candidato.setLinguagemList(linguagemList);
            candidato.setEdicao(candidatoEntity.getEdicao().getNome());
            candidato.setGenero(candidatoEntity.getGenero());
            candidato.setCidade(candidatoEntity.getCidade());
            candidato.setEstado(candidatoEntity.getEstado());
            candidato.setObservacoes(candidatoEntity.getObservacoes());
            if(candidatoEntity.getCurriculoEntity()== null){
                throw new RegraDeNegocioException("O candidato com o email " +candidatoEntity.getEmail() +" não possui currículo cadastrado!");
            }
            candidato.setDado(candidatoEntity.getCurriculoEntity().getDado());
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                relatorioCandidatoCadastroDTOPage);
    }

    public PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage = candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto.trim(), nomeTrilha, pageRequest);
        if (candidatoEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoEntityPage.toList());
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
                .collect(Collectors.toSet()));
        return candidatoDTO;
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        candidatoRepository.deleteById(id);
    }
}
