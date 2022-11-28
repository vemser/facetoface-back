package br.com.vemser.facetoface.controller;

import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.dto.login.LoginRetornoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
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
                                                       @PathVariable Genero genero) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.createUsuario(usuarioCreateDTO, genero), HttpStatus.OK);
    }

//    @PostMapping("/admin")
//    public ResponseEntity<UsuarioDTO> cadastrarUsuarioAdmin(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.createAdmin(usuarioCreateDTO, 1), HttpStatus.OK);
//    }

    @GetMapping
    public PageDTO<UsuarioDTO> list(@RequestParam(defaultValue = "0") Integer pagina,
                                      @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException {
        return usuarioService.list(pagina, tamanho);
    }

    @GetMapping("/email/{email}")
    public UsuarioDTO findByEmail(@RequestParam String email) throws RegraDeNegocioException {
        return usuarioService.findByEmailDTO(email);
    }

    @GetMapping("/id/{id}")
    public UsuarioEntity findById(@RequestParam Integer id) throws RegraDeNegocioException {
        return usuarioService.findById(id);
    }

    @GetMapping("/logado")
    public LoginRetornoDTO getLoggedUser() throws RegraDeNegocioException {
        return usuarioService.getLoggedUser();
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable("idUsuario") Integer id,
                                               @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        UsuarioDTO usuarioDTO = usuarioService.update(id, usuarioCreateDTO);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
