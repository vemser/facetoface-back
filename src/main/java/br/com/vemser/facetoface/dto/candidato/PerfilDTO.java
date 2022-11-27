package br.com.vemser.facetoface.dto.candidato;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PerfilDTO {
    @Schema(example = "1")
    private int id;

    @Schema(description = "Tipo de perfil do Usuário.")
    private String nome;
}
