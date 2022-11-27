package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.repository.EdicaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EdicaoService {
    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;

    public EdicaoEntity create(EdicaoDTO edicaoDTO){
        edicaoDTO.setNome(edicaoDTO.getNome().trim().toUpperCase());
        return edicaoRepository.save(converterEntity(edicaoDTO));
    }
    public EdicaoEntity findByNome(String nome){
        nome = nome.trim().toUpperCase();
        return edicaoRepository.findByNome(nome)
                .orElse(create(new EdicaoDTO(nome)));
    }

    private EdicaoEntity converterEntity(EdicaoDTO edicaoDTO) {
        return objectMapper.convertValue(edicaoDTO, EdicaoEntity.class);
    }
}
