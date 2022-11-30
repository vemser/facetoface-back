package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.UserSenhaDTO;
import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface OperationControllerAuth {

    @Operation(summary = "Logar com um usuário.", description = "Loga no sistema com um login de usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Loga no sistema com um login de usuário."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    String auth(@RequestBody @Valid LoginDTO loginDTO);

    @Operation(summary = "Solicitar uma troca de senha do usuário.", description = "Solicita uma troca de senha do usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Solicita uma troca de senha do usuário."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    void trocarSenha() throws RegraDeNegocioException;

    @Operation(summary = "Solicitar uma troca de senha do usuário.", description = "Solicita uma troca de senha do usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Solicita uma troca de senha do usuário."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    void trocarSenhaAuntenticado(@RequestBody @Valid UserSenhaDTO userSenhaDTO) throws RegraDeNegocioException;
}
