package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/candidato")
public class CandidatoController {
    private final CandidatoService candidatoService;

    @GetMapping
    public PageDTO<CandidatoDTO> list(@RequestParam(defaultValue = "0") Integer pagina,
                                      @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException {
        return candidatoService.list(pagina, tamanho);
    }

    @GetMapping("/findbyemails/{email}")
    public CandidatoDTO findByEmail(@PathVariable("email") String email) throws RegraDeNegocioException {
        return candidatoService.findByEmail(email);
    }

    @GetMapping("/findbynomecompleto/{nomeCompleto}")
    public CandidatoDTO findByNomeCompleto(@PathVariable("nomeCompleto") String nomeCompleto) throws RegraDeNegocioException {
        return candidatoService.findByNomeCompleto(nomeCompleto);
    }

    @PostMapping
    public ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @PutMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> update(@PathVariable("idCandidato") Integer id,
                                                 @Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.update(id, candidatoCreateDTO);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> delete(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException {
        candidatoService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
