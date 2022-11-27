package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.CurriculoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import br.com.vemser.facetoface.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository curriculoRepository;

    public CurriculoEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return curriculoRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }
}
