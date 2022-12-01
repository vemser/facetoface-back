package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.controller.documentationinterface.AuthControllerInterface;
import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.dto.login.UserSenhaDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.security.TokenService;
import br.com.vemser.facetoface.service.AuthService;
import br.com.vemser.facetoface.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements AuthControllerInterface {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthService authService;


    @PostMapping("/fazer-login")
    public ResponseEntity<String> auth(@RequestBody @Valid LoginDTO loginDTO) {
        UsuarioEntity usuarioEntity = authService.auth(loginDTO);
        return new ResponseEntity<>(tokenService.getToken(usuarioEntity, null), HttpStatus.CREATED);
    }

    @PostMapping("/solicitar-troca-senha")
    public void trocarSenha() throws RegraDeNegocioException {
        authService.trocarSenha();
        new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/trocar-senha")
    public void trocarSenhaAuntenticado(@RequestParam String token) throws RegraDeNegocioException {
        String email = authService.procurarUsuario(token);
        usuarioService.atualizarSenhaUsuario(email);
        new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/confirmar-entrevista/{tokenEntrevista}")
    public void confirmarEntrevista(@PathVariable @Valid String tokenEntrevista) throws RegraDeNegocioException {
        authService.confirmarEntrevista(tokenEntrevista);
        new ResponseEntity<>(null, HttpStatus.OK);
    }
}
