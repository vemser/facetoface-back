package br.com.vemser.facetoface.dto.entrevista;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntrevistaCreateDTO {

    @NotNull
    @Schema(description = "Candidato que irá participar da entrevista.")
    private String candidatoNome;

    @NotNull
    @Schema(description = "Usuário que irá realizar a entrevista.")
    private String usuarioNome;

    @NotNull
    @Schema(description = "Dia/Mês/Ano que irá ocorrer a entrevista.")
    private LocalDateTime dataEntrevista;

    @NotNull
    @Schema(description = "Cidade em que o usuário irá realizar a entrevista")
    private String cidade;

    @NotNull
    @Schema(description = "Estado em que o usuário irá realizar a entrevista")
    private String estado;

//    @NotNull
//    @Schema(description = "Hora/Min/Seg que irá ocorrer a entrevista.")
//    private LocalTime horasMin;

    @NotNull
    @Schema(description = "Observações referentes a entrevista.")
    private String observacoes;
}
