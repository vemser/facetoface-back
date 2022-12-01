package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.login.LoginRetornoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

public interface UsuarioControllerInterface {

    @Operation(summary = "Criar Usuario", description = "Criar usuario no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro de usuario realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PostMapping
    ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                                       @RequestParam Genero genero) throws RegraDeNegocioException, IOException, MessagingException, TemplateException;

    @Operation(summary = "Procurar usuario por email", description = "Procura o e-mail do usuário cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    UsuarioDTO findByEmail(@RequestParam String email) throws RegraDeNegocioException;

    @Operation(summary = "Procurar usuario logado", description = "Procura o usuário que esta logado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    LoginRetornoDTO getLoggedUser() throws RegraDeNegocioException;

    @Operation(summary = "Procurar usuario pelo nome completo", description = "Procura o usuário pelo nome completo que esta no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    PageDTO<UsuarioDTO> findByNomeCompleto(@RequestParam("nomeCompleto") String nomeCompleto,
                                                  @RequestParam(defaultValue = "0") Integer pagina,
                                                  @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Listar usuarios por página", description = "Lista os usuários presentes no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    PageDTO<UsuarioDTO> list(@RequestParam(defaultValue = "0") Integer pagina,
                                    @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Procurar usuario pelo id", description = "Procura o usuário pelo id que esta no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    UsuarioDTO findById(@RequestParam Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar o usuário no sistema.", description = "Atualiza o usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atualiza o usuário no sistema.!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    public ResponseEntity<UsuarioDTO> update(@PathVariable("idUsuario") Integer id,
                                             @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                             Genero genero) throws RegraDeNegocioException;

    @Operation(summary = "Deleta o usuário logicamente no sistema", description = "Deleta o usuário logicamente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<UsuarioDTO> delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Deleta o usuário fisicamente/definitivamente no sistema", description = "Deleta o usuário fisicamente/definitivamente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<UsuarioDTO> deleteFisico(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Inserir imagem no sistema", description = "Insere imagem do usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem inseria com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                           @RequestParam("email") String email) throws RegraDeNegocioException, IOException;


    @Operation(summary = "Recuperar imagem no sistema", description = "Recupera a imagem do usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem recuperada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException;

    }
