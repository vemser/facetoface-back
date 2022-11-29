package br.com.vemser.facetoface.dto.entrevista;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrevistaCreateDTO {

    @NotNull
    @Schema(description = "Candidato que irá participar da entrevista.")
    private String candidatoEmail;

    @NotNull
    @Schema(description = "Usuário que irá realizar a entrevista.")
    private String usuarioEmail;

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
}
