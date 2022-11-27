package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.security.TokenService;
import br.com.vemser.facetoface.service.AuthService;
import br.com.vemser.facetoface.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthService authService;


    @PostMapping("/fazer-login")
    public ResponseEntity<String> auth(@RequestBody @Valid LoginDTO loginDTO) {
        UsuarioEntity usuarioEntity = authService.auth(loginDTO);
        return new ResponseEntity<>(tokenService.getToken(usuarioEntity, null), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<LoginDTO> retornarUsuarioLogado() {
//        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
//    }
//
//    @PostMapping("/solicitar-troca-senha/{cpf}")
//    public void trocarSenha(@PathVariable("cpf") String cpf) throws RegraDeNegocioException {
//        authService.trocarSenha(cpf);
//        new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    @PostMapping("/trocar-senha")
//    public void trocarSenhaAuntenticado(@RequestBody @Valid UserSenhaDTO userSenhaDTO) throws RegraDeNegocioException {
//        String cpf = authService.procurarUsuario(userSenhaDTO.getToken());
//        funcionarioService.atualizarSenhaFuncionario(cpf, userSenhaDTO.getSenha());
//        new ResponseEntity<>(null, HttpStatus.OK);
//    }

}
