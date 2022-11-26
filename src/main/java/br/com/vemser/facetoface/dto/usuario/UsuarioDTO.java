package br.com.vemser.facetoface.dto.usuario;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO extends UsuarioCreateDTO {
    @NotNull
    private Integer idUsuario;
}
