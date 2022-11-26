package br.com.vemser.facetoface.dto.candidato;

import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Data
public class PerfilDTO {

    @NotNull
    @Schema(description = "Tipo de perfil do Usuário.")
    private String nome;
}
