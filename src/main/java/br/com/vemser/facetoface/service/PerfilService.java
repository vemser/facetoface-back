package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.PerfilDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.PerfilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final ObjectMapper objectMapper;

    public PerfilEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Perfil não encontrado!"));
    }

    public List<PerfilEntity> listarPerfis() {
        return perfilRepository.findAll();
    }

    public List<String> convertToStringList(Set<PerfilEntity> perfilEntities) {
        return perfilEntities.stream()
                .map(PerfilEntity::getNome)
                .toList();
    }

    public PerfilDTO convertToDTO(PerfilEntity perfilEntity) {
        return objectMapper.convertValue(perfilEntity, PerfilDTO.class);
    }
}
