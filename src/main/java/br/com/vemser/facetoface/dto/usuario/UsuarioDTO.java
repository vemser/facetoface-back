package br.com.vemser.facetoface.dto.usuario;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioDTO {

    @NotNull
    private Integer idUsuario;
}
