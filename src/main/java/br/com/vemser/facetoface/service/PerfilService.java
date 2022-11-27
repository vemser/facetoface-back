package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilEntity findById(Integer idPerfil) throws RegraDeNegocioException{
        return perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Perfil não encontrado!"));
    }
}
