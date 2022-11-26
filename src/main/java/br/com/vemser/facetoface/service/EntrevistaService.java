package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.repository.EntrevistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;

}
