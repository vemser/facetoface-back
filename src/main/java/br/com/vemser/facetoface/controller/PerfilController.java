package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfil")
public class PerfilController {
    private final PerfilService perfilService;

    @GetMapping
    public List<PerfilEntity> getPerfils() {
        return perfilService.listarPerfis();
    }
}
