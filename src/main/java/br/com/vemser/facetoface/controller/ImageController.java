package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.CurriculoDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CurriculoService;
import br.com.vemser.facetoface.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping("/delete-fisico/{idImagem}")
    public ResponseEntity<CurriculoDTO> deleteFisico(@PathVariable("idImagem") Integer id) throws RegraDeNegocioException {
        imageService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
