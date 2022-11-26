package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatoService {
    private final CandidatoRepository candidatoRepository;
    private final ObjectMapper objectMapper;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO){
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public List<CandidatoDTO> list(){
        List<CandidatoDTO> candidatoDTOList = candidatoRepository.findAll().stream()
                .map(this::converterEmDTO)
                .toList();
        return candidatoDTOList;
    }

    private CandidatoEntity findById(Integer id) throws RegraDeNegocioException {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Candidato não encontrado."));
    }

    private CandidatoEntity converterEntity(CandidatoCreateDTO candidatoCreateDTO) {
        return objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
    }

    private CandidatoDTO converterEmDTO(CandidatoEntity candidatoEntity) {
        return objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
    }
}
