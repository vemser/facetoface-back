package br.com.vemser.facetoface.controller.documentationinterface;

import br.com.vemser.facetoface.dto.RelatorioCandidatoCadastroDTO;
import br.com.vemser.facetoface.dto.RelatorioCandidatoPaginaPrincipalDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

public interface OperationControllerCandidato {

    @Operation(summary = "Listagem de candidatos no sistema", description = "Listagem dos candidatos presentes no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de usuários realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    PageDTO<CandidatoDTO> list(@RequestParam(defaultValue = "0") Integer pagina,
                                      @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Procurar candidato no sistema por email", description = "Procura o candidato pelo e-mail se estiver cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pesquisa pelo email realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    CandidatoDTO findByEmail(@PathVariable("email") String email) throws RegraDeNegocioException;

    @Operation(summary = "Criar cadastro de Candidato", description = "Criar candidato no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de candidato realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PostMapping
    ResponseEntity<CandidatoDTO> create(@Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO,
                                               Genero genero) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Atualizar cadastro de candidato", description = "Atualizar candidato no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização de cadastro de candidato realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<CandidatoDTO> update(@PathVariable("idCandidato") Integer id,
                                               @Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO,
                                        Genero genero) throws RegraDeNegocioException;

    @Operation(summary = "Deletar cadastro de Usuario", description = "Deletar Usuario no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário removido do sistema com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<CandidatoDTO> delete(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Inserir imagem para o cadastro de Usuario", description = "Inserir imagem do Usuario no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem inserida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                              @RequestParam("email") String email) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Recuperar imagem cadastrada no sistema", description = "Recupera a imagem de um usuário especifico do sistema pelo e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem recuperada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<String> recuperarImagem(@RequestParam("email") String email) throws RegraDeNegocioException;

    @Operation(summary = "Inserir curriculo do Candidato no sistema", description = "Cadastrar curriculo de um candidato especifico do sistema pelo e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<Void> uploadCurriculo(@RequestPart("file")MultipartFile file,
                                                        @RequestParam("email") String email) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Recuperar curriculo cadastrado no sistema", description = "Recupera o curriculo de um candidato especifico do sistema pelo e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curriculo recuperado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<String> recuperarCurriculo(@RequestParam("email") String email) throws RegraDeNegocioException;

    @Operation(summary = "Deletar o cadastro no banco de Usuario", description = "Deletaro cadastro no banco Usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário removido do sistema com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<CandidatoDTO> deleteFisico(@PathVariable("idCandidato") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Listar relatórios principais de candidatos.", description = "Lista relatórios de candidatos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listando relatórios de candidatos."),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(@RequestParam(value = "nomeCompleto", required = false) String nomeCompleto,
                                                                                                    @RequestParam(defaultValue = "0") Integer pagina,
                                                                                                    @RequestParam(defaultValue = "20") Integer tamanho,
                                                                                                    @RequestParam(required = false) String nomeTrilha) throws RegraDeNegocioException;

    @Operation(summary = "Listar relatórios de cadastro de candidatos.", description = "Lista relatórios de cadastro de candidatos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listando relatórios de cadastro de candidatos."),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(@RequestParam(value = "nomeCompleto", required = false) String nomeCompleto,
                                                                             @RequestParam(defaultValue = "0") Integer pagina,
                                                                             @RequestParam(defaultValue = "20") Integer tamanho,
                                                                             @RequestParam(required = false) String nomeTrilha) throws RegraDeNegocioException;
}
