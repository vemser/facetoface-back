package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.EdicaoControllerInterface;
import br.com.vemser.facetoface.dto.edicao.EdicaoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.EdicaoService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/edicao")
public class EdicaoController implements EdicaoControllerInterface {
    private final EdicaoService edicaoService;

    @DeleteMapping("/delete-fisico/{idEdicao}")
    public ResponseEntity<EdicaoEntity> deleteFisico(@PathVariable("idEdicao") Integer id) throws RegraDeNegocioException {
        edicaoService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/criar-edicao")
    public ResponseEntity<EdicaoDTO> cadastrarEdicao(@Valid @RequestBody EdicaoDTO edicaoDTO)  {
        return new ResponseEntity<>(edicaoService.createAndReturnDTO(edicaoDTO), HttpStatus.CREATED);
    }
}
