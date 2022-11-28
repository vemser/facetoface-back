package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

public interface OperationControllerUsuario {

    @Operation(summary = "Criar Usuario", description = "Criar usuario no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de usuario realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO,
                                                       @PathVariable Genero genero) throws RegraDeNegocioException;

    @Operation(summary = "Procurar usuario por email", description = "Procura o e-mail do usuário cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public Optional<UsuarioEntity> findByEmail(@RequestParam String email);

    @Operation(summary = "Procurar usuario por id", description = "Procura o usuário pelo id cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public UsuarioEntity findById(@RequestParam Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Procurar usuario logado", description = "Procura o usuário que esta logado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procura realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException;

    }
