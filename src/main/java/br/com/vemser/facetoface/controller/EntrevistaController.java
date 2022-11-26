package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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

    @GetMapping("/listar-entrevistas-por-mes")
    public List<EntrevistaDTO> listPorMes() throws RegraDeNegocioException {
        return entrevistaService.listarEntrevistas();
    }

    @PutMapping("/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO, @PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException{
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO), HttpStatus.OK);
    }

    @PostMapping("/marcar-entrevista")
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException{
        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO), HttpStatus.OK);
    }

//    @PostMapping("/agendar-entrevista")
//    public ResponseEntity<EntrevistaDTO> marcarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException{
//        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO), HttpStatus.OK);
//    }
//    @GetMapping("/listar-entrevistas-por-mes")
//    public void listarEntrevistasPorMes(){
//
//    }
//
//    @GetMapping("/{nomeUsuario}")
//    public void listarEntrevistaPorUsuario(){
//
//    }
}
