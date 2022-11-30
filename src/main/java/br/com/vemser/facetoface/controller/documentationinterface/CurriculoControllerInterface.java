package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.CurriculoDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CurriculoControllerInterface {
    @Operation(summary = "Deleta o currículo no sistema", description = "Deleta o currículo no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<CurriculoDTO> deleteFisico(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;
}
