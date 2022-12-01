package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.perfil.PerfilDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.PerfilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final ObjectMapper objectMapper;

    public PerfilEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Perfil não encontrado!"));
    }

    public PerfilEntity findByNome(String nome) throws RegraDeNegocioException {
        nome = nome.trim().toUpperCase();
        return perfilRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Perfil não encontrado!"));
    }

    public PerfilDTO convertToDTO(PerfilEntity perfilEntity) {
        return objectMapper.convertValue(perfilEntity, PerfilDTO.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        perfilRepository.deleteById(id);
    }
}
