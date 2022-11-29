package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.CurriculoDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CurriculoService;
import br.com.vemser.facetoface.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
