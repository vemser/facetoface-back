package br.com.vemser.facetoface.dto.usuario;

import br.com.vemser.facetoface.dto.PerfilDTO;
import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {
    @NotNull
    @Schema(example = "Priscila Matos", description = "Nome completo do usuário.")
    private String nomeCompleto;

    @NotNull
    @Schema(example = "Canoas", description = "Cidade onde mora o candidato.")
    private String cidade;

    @NotNull
    @Schema(example = "Rio Grande do Sul", description = "Estado onde mora o candidato.")
    private String estado;

    @NotNull
    @Schema(example = "priscila.matos@dbccompany.com.br", description = "E-mail do candidato.")
    private String email;

    @NotNull
    @Schema(description = "Trilha que o candidato participa.")
    private TrilhaDTO trilha;

    private List<PerfilDTO> perfis;

    @NotNull
    @Schema(description = "Usuário ativo ou não. 'T' para ativo e 'F' para inativo.", example = "T")
    private char ativo;

}
