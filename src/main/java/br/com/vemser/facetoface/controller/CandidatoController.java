package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.OperationControllerCandidato;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

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
    public PageDTO<CandidatoDTO> findByNomeCompleto(@PathVariable("nomeCompleto") String nomeCompleto,
                                           @RequestParam(defaultValue = "0") Integer pagina,
                                           @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException {
        return candidatoService.findByNomeCompleto(nomeCompleto, pagina, tamanho);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO,
                                               @RequestPart(name = "question-image", required = false) MultipartFile imagem,
                                               @RequestPart(name = "question-curriculo") MultipartFile curriculo,
                                               Genero genero) throws RegraDeNegocioException, IOException {
        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO, imagem, curriculo,  genero);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @PutMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> update(@PathVariable("idCandidato") Integer id,
                                                 @Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO,
                                               Genero genero) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.update(id, candidatoCreateDTO, genero);
        return new ResponseEntity<>(candidatoDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idCandidato}")
    public ResponseEntity<CandidatoDTO> delete(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException {
        candidatoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/upload-foto/{emailCandidato}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadFoto(@RequestPart("file")MultipartFile file,
                                              @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
            candidatoService.arquivarCandidato(file, email);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-imagem")
    public ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(candidatoService.pegarImagemCandidato(email), HttpStatus.OK);
    }

    @PutMapping(value = "/upload-curriculo/{emailCandidato}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadCurriculo(@RequestPart("file")MultipartFile file,
                                                @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
        candidatoService.arquivarCurriculo(file, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-curriculo")
    public ResponseEntity<String> recuperarCurriculo(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(candidatoService.pegarCurriculoCandidato(email), HttpStatus.OK);
    }
}
