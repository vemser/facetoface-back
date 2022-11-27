package br.com.vemser.facetoface.dto.candidato;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.Trilha;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoCreateDTO {

    @NotNull
    @Schema(description = "Nome completo do candidato.")
    private String nomeCompleto;

    @NotNull
    @Schema(description = "Cidade do candidato.")
    private String cidade;

    @NotNull
    @Schema(description = "Estado do candidato.")
    private String estado;

    @NotNull
    @Schema(description = "E-mail do candidato.")
    private String email;

    @NotNull
    @Schema(description = "Gênero do candidato.")
    private Genero genero;

    private byte[] foto;

    @NotNull
    @Schema(description = "Trilha que o candidato participa.")
    private Trilha trilha;

    @NotNull
    @Schema(description = "Número da edição que o candidato participa.")
    private EdicaoDTO edicao;

    @NotNull
    @Schema(description = "Nota da prova do candidato.")
    private Double notaProva;

    @NotNull
    @Schema(description = "Lista de linguagens que o candidato conhece.")
    private List<LinguagemDTO> linguagens;

    @NotNull
    @Schema(description = "Observações referentes ao candidato.")
    private String observacoes;

    @NotNull
    @Schema(description = "Usuário ativo ou não. 'T' para ativo e 'F' para inativo.", example = "T")
    private char ativo;

    private byte[] curriculo;
}
