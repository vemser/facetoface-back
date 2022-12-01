package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.linguagem.LinguagemDTO;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.LinguagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        nome = nome.trim().toUpperCase();
        Optional<LinguagemEntity> linguagemEntity = linguagemRepository.findByNome(nome);
        if(linguagemEntity.isEmpty()){
            return create(new LinguagemDTO(nome));
        }
        return linguagemEntity.get();
    }

    public LinguagemEntity findById(Integer idLinguagem) throws RegraDeNegocioException {
        return linguagemRepository.findById(idLinguagem)
                .orElseThrow(() -> new RegraDeNegocioException("Linguagem não encontrada!"));
    }

    public LinguagemEntity converterEntity(LinguagemDTO linguagemDTO) {
        return objectMapper.convertValue(linguagemDTO, LinguagemEntity.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        linguagemRepository.deleteById(id);
    }
}
