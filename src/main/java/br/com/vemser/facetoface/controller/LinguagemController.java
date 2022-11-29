package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.LinguagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/linguagem")
public class LinguagemController {
    private final LinguagemService linguagemService;

    @DeleteMapping("/delete-fisico/{idLinguagem}")
    public ResponseEntity<UsuarioDTO> deleteFisico(@PathVariable("idLinguagem") Integer id) throws RegraDeNegocioException {
        linguagemService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
