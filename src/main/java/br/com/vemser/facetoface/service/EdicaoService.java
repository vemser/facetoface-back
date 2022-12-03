package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.edicao.EdicaoDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;
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

    public EdicaoDTO createAndReturnDTO(EdicaoDTO edicaoDTO) {
        edicaoDTO.setNome(edicaoDTO.getNome().trim());
        EdicaoEntity edicaoEntity = edicaoRepository.save(converterEntity(edicaoDTO));
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
    }

    public EdicaoEntity findByNome(String nome) throws RegraDeNegocioException {
        Optional<EdicaoEntity> edicaoEntity = edicaoRepository.findByNome(nome);
        if (edicaoEntity.isEmpty()) {
            throw new RegraDeNegocioException("Edição não encontrada!");
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
