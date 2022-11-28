package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.Month;

public interface OperationControllerEntrevista {

    @Operation(summary = "Listagem de entrevistas no sistema", description = "Listagem das entrevistas presentes no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de entrevistas realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<PageDTO<EntrevistaDTO>> list(@RequestParam(defaultValue = "0") Integer pagina,
                                                       @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Listagem de entrevistas por usuarios no sistema", description = "Listagem das entrevistas de acordo com o usuário selecionado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem  realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<PageDTO<EntrevistaDTO>> listarMes(@RequestParam(defaultValue = "0") Integer pagina,
                                                            @RequestParam(defaultValue = "20") Integer tamanho,
                                                            @PathVariable("mes") Month mes) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar entrevista de candidato", description = "Atualizar entrevista no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização de entrevista de candidato realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO, @PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Criar entrevista para Candidatos", description = "Criar entrevistas no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de entrevista realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar cadastro de Entrevista", description = "Deletar entrevista no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrevista removida do sistema com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    public ResponseEntity<EntrevistaDTO> deletarEntrevista(@PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException;
}
