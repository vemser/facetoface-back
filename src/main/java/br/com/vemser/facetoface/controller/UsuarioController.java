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
import br.com.vemser.facetoface.service.ImageService;
import br.com.vemser.facetoface.service.UsuarioService;
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
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                                       @RequestParam Genero genero) throws RegraDeNegocioException, IOException {
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

    @GetMapping("/email")
    public UsuarioDTO findByEmail(@RequestParam String email) throws RegraDeNegocioException {
        return usuarioService.findByEmailDTO(email);
    }

    @GetMapping("/id")
    public UsuarioEntity findById(@RequestParam Integer id) throws RegraDeNegocioException {
        return usuarioService.findById(id);
    }

    @GetMapping("/logado")
    public LoginRetornoDTO getLoggedUser() throws RegraDeNegocioException {
        return usuarioService.getLoggedUser();
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable("idUsuario") Integer id,
                                               @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                             Genero genero) throws RegraDeNegocioException {
        UsuarioDTO usuarioDTO = usuarioService.update(id, usuarioCreateDTO, genero);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/upload-foto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                           @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
        imageService.arquivarUsuario(file, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-imagem")
    public ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(imageService.pegarImagemUsuario(email), HttpStatus.OK);
    }
}
