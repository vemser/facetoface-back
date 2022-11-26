package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.repository.LinguagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LinguagemService {
    private final LinguagemRepository linguagemRepository;
    private final ObjectMapper objectMapper;

    public LinguagemEntity create(LinguagemDTO linguagemDTO){
        linguagemDTO.setNome(linguagemDTO.getNome().trim().toUpperCase());
        return linguagemRepository.save(converterEntity(linguagemDTO));
    }
    public LinguagemEntity findByNome(String nome){
        return linguagemRepository.findByNome(nome)
                .orElse(create(new LinguagemDTO(nome)));
    }

    private LinguagemEntity converterEntity(LinguagemDTO linguagemDTO) {
        return objectMapper.convertValue(linguagemDTO, LinguagemEntity.class);
    }
}
