package br.com.vemser.facetoface.dto.usuario;

import br.com.vemser.facetoface.entity.enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO extends UsuarioCreateDTO {
    @Schema(example = "1")
    private Integer idUsuario;
    private Genero genero;
}
