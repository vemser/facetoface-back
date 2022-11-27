package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

//    @PostMapping
//    public ResponseEntity<UsuarioEntity> cadastrarEntrevista(@Valid @RequestBody UsuarioEntity entrevistaCreateDTO) throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.createUsuario(entrevistaCreateDTO), HttpStatus.OK);
//    }
}
