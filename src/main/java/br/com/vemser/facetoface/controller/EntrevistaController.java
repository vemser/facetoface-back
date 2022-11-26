package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/entrevista")
public class EntrevistaController {

    private final EntrevistaService entrevistaService;

    @GetMapping
    public List<EntrevistaDTO> list() throws RegraDeNegocioException {
        return entrevistaService.listarEntrevistas();
    }

    @PutMapping("/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO, @PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException{
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO), HttpStatus.OK);
    }


}
