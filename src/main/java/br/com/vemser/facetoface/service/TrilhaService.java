package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.TrilhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final TrilhaRepository trilhaRepository;

    public TrilhaEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return trilhaRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Trilha não encontrada!"));
    }
}
