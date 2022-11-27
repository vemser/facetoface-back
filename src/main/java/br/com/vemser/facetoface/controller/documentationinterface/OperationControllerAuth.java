package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface OperationControllerAuth {

    @Operation(summary = "Login", description = "Realiza o seu login com email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Email ou senha incorretos. Login não concluído.")
    })
    public String auth(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException;
}
