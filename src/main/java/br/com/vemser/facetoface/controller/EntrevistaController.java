package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.EntrevistaControllerInterface;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EntrevistaService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/entrevista")
public class EntrevistaController implements EntrevistaControllerInterface {

    private final EntrevistaService entrevistaService;

    @PutMapping("/atualizar-entrevista/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                                          @PathVariable("idEntrevista") Integer id,
                                                          Legenda legenda) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO, legenda), HttpStatus.OK);
    }

    @PostMapping("/marcar-entrevista")
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO), HttpStatus.CREATED);
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

    @GetMapping("/listar-por-mes")
    public ResponseEntity<PageDTO<EntrevistaDTO>> listarMesAno(@RequestParam(defaultValue = "0") Integer pagina,
                                                               @RequestParam(defaultValue = "20") Integer tamanho,
                                                               @RequestParam Integer mes,
                                                               @RequestParam Integer ano) {
        return new ResponseEntity<>(entrevistaService.listMes(pagina, tamanho, mes, ano), HttpStatus.OK);
    }

    @PutMapping("/atualizar-observacao-entrevista/{idEntrevista}")
    public ResponseEntity<Void> atualizarEntrevista(@PathVariable ("idEntrevista") Integer id,
                                                     String observacao) throws RegraDeNegocioException {
        entrevistaService.atualizarObservacaoEntrevista(id, observacao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-entrevista-email-candidato/{email}")
    public ResponseEntity<EntrevistaDTO> buscarEntrevistaPorEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.buscarPorEmailCandidato(email), HttpStatus.OK);
    }

    @DeleteMapping("/deletar-entrevista-email-candidato/{email}")
    public ResponseEntity<Void> deletarEntrevistaEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException{
        entrevistaService.deletarEntrevistaEmail(email);
        return ResponseEntity.noContent().build();
    }
}
