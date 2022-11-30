package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.CurriculoDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CurriculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/curriculo")
public class CurriculoController {
    private final CurriculoService curriculoService;

    @DeleteMapping("/delete-fisico/{idCurriculo}")
    public ResponseEntity<CurriculoDTO> deleteFisico(@PathVariable("idCurriculo") Integer id) throws RegraDeNegocioException {
        curriculoService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
