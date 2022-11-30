package br.com.vemser.facetoface.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EntrevistaAtualizacaoDTO {

    @NotNull
    @Schema(description = "Dia/Mês/Ano que irá ocorrer a entrevista.")
    private LocalDateTime dataEntrevista;

    @NotNull
    @Schema(description = "Cidade em que o usuário irá realizar a entrevista")
    private String cidade;

    @NotNull
    @Schema(description = "Estado em que o usuário irá realizar a entrevista")
    private String estado;

    @NotNull
    @Schema(description = "Observações referentes a entrevista.")
    private String observacoes;

    private String email;

}
