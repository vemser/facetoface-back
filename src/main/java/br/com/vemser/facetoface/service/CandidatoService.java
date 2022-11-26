package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidatoService {
    private final CandidatoRepository candidatoRepository;
}
