package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/candidato")
public class CandidatoController {
    private final CandidatoService candidatoService;

    @GetMapping
    public List<CandidatoDTO> list() throws RegraDeNegocioException {
        return candidatoService.list();
    }

    @PostMapping
    public ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO){
        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }
}
