package br.com.vemser.facetoface.dto.candidato;

import br.com.vemser.facetoface.entity.Edicao;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.Linguagem;
import br.com.vemser.facetoface.entity.enums.Trilha;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
    @Schema(description = "Edição que o candidato participa.")
    private Edicao edicao;

    @NotNull
    @Schema(description = "Nota da prova do candidato.")
    private Double notaProva;

    @NotNull
    @Schema(description = "Lista de linguagens que o candidato conhece.")
    private List<Linguagem> linguagens;

    @NotNull
    @Schema(description = "Observações referentes ao candidato.")
    private String observacoes;

    private byte[] curriculo;
}
