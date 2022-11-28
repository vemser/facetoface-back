package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.OperationControllerEntrevista;
import br.com.vemser.facetoface.dto.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/entrevista")
public class EntrevistaController {

    private final EntrevistaService entrevistaService;

    @PutMapping("/atualizar-entrevista/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                                          @PathVariable("idEntrevista") Integer id,
                                                          Legenda legenda) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO, legenda), HttpStatus.OK);
    }

    @PostMapping("/marcar-entrevista")
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> deletarEntrevista(@PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException {
        entrevistaService.deletarEntrevista(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<EntrevistaDTO>> list(@RequestParam(defaultValue = "0") Integer pagina,
                                                       @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.list(pagina, tamanho), HttpStatus.OK);
    }
}
