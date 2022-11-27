package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private static final int ROLE_ADMIN_ID = 1;
    private static final int ROLE_GESTAO_ID = 2;
    private static final int ROLE_INSTRUTOR_ID = 3;
    private final UsuarioService usuarioService;

    @PostMapping("/perfil/{idPerfil}/trilha/{idTrilha}/genero/{genero}")
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                                       @PathVariable int idPerfil,
                                                       @PathVariable int idTrilha,
                                                       @PathVariable Genero genero) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.createUsuario(usuarioCreateDTO, idPerfil, idTrilha, genero), HttpStatus.OK);
    }

//    @PostMapping("/admin")
//    public ResponseEntity<UsuarioDTO> cadastrarUsuarioAdmin(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.createAdmin(usuarioCreateDTO, 1), HttpStatus.OK);
//    }

    @GetMapping("/email/{email}")
    public Optional<UsuarioEntity> findByEmail(@RequestParam String email) {
        return usuarioService.findByEmail(email);
    }

    @GetMapping("/id/{id}")
    public UsuarioEntity findById(@RequestParam Integer id) throws RegraDeNegocioException {
        return usuarioService.findById(id);
    }

    @GetMapping("/logado")
    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        return usuarioService.listarUsuarioLogado();
    }
}
