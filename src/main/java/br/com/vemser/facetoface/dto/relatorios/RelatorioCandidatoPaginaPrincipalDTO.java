package br.com.vemser.facetoface.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioCandidatoPaginaPrincipalDTO {
    private Integer idCandidato;
    private String nomeCompleto;
    private String email;
    private Double notaProva;
    private String trilha;
}
