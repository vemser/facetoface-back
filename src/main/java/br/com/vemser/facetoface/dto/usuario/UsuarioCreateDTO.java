package br.com.vemser.facetoface.dto.usuario;

import br.com.vemser.facetoface.dto.candidato.PerfilDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.enums.Trilha;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {

    @NotNull
    @Schema(description = "Nome completo do usuário.")
    private String nomeCompleto;

    @NotNull
    @Schema(description = "Cidade onde mora o candidato.")
    private String cidade;

    @NotNull
    @Schema(description = "Estado onde mora o candidato.")
    private String estado;

    @NotNull
    @Schema(description = "E-mail do candidato.")
    private String email;

    @NotNull
    @Schema(description = "Genêro do candidato.")
    private Genero genero;

    @Schema(description = "Foto do candidato (Opcional).")
    private byte[] foto;

    @NotNull
    @Schema(description = "Trilha em que o candidato faz parte.")
    private Trilha trilha;

    @NotNull
    @Schema(description = "Tipo de perfil do candidato.")
    private PerfilDTO perfil;
}
