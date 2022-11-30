package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EdicaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/edicao")
public class EdicaoController {
    private final EdicaoService edicaoService;

    @DeleteMapping("/delete-fisico/{idEdicao}")
    public ResponseEntity<EdicaoEntity> deleteFisico(@PathVariable("idEdicao") Integer id) throws RegraDeNegocioException {
        edicaoService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
