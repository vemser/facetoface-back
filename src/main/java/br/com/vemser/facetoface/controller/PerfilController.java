package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfil")
public class PerfilController {
    private final PerfilService perfilService;

    @DeleteMapping("/delete-fisico/{idPerfil}")
    public ResponseEntity<UsuarioDTO> deleteFisico(@PathVariable("idPerfil") Integer id) throws RegraDeNegocioException {
        perfilService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
