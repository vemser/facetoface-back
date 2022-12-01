package br.com.vemser.facetoface.dto.perfil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    @Schema(description = "Nome do perfil do usuário.", example = "ROLE_INSTRUTOR")
    private String nome;
}
