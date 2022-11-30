package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EdicaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EdicaoService {
    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;

    public EdicaoEntity findById(Integer idEdicao) throws RegraDeNegocioException {
        return edicaoRepository.findById(idEdicao)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada!"));
    }

    public EdicaoEntity create(EdicaoDTO edicaoDTO){
        edicaoDTO.setNome(edicaoDTO.getNome().trim().toUpperCase());
        return edicaoRepository.save(converterEntity(edicaoDTO));
    }
    public EdicaoEntity findByNome(String nome){
        nome = nome.trim().toUpperCase();
        Optional<EdicaoEntity> edicaoEntity = edicaoRepository.findByNome(nome);
        if(edicaoEntity.isEmpty()){
            return create(new EdicaoDTO(nome));
        }
        return edicaoEntity.get();
    }

    public EdicaoEntity converterEntity(EdicaoDTO edicaoDTO) {
        return objectMapper.convertValue(edicaoDTO, EdicaoEntity.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        edicaoRepository.deleteById(id);
    }
}
