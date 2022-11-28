package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.OperationControllerCandidato;
import br.com.vemser.facetoface.dto.CurriculoDTO;
import br.com.vemser.facetoface.dto.FotoDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.CandidatoService;
import br.com.vemser.facetoface.service.CurriculoService;
import br.com.vemser.facetoface.service.ImageService;
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
public class CandidatoController implements OperationControllerCandidato {
    private final CandidatoService candidatoService;
    private final CurriculoService curriculoService;
    private final ImageService imageService;

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

    @PostMapping
    public ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO,
                                               @PathVariable Genero genero) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO, genero);
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

    @PutMapping(value = "/upload-foto/{emailCandidato}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadFoto(@RequestPart("file")MultipartFile file,
                                              @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
            imageService.arquivarCandidato(file, email);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-imagem")
    public ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(imageService.pegarImagemUsuario(email), HttpStatus.OK);
    }

    @PutMapping(value = "/upload-curriculo/{emailCandidato}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadCurriculo(@RequestPart("file")MultipartFile file,
                                                @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
        curriculoService.arquivarCurriculo(file, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-curriculo")
    public ResponseEntity<String> recuperarCurriculo(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(curriculoService.pegarCurriculoCandidato(email), HttpStatus.OK);
    }
}
