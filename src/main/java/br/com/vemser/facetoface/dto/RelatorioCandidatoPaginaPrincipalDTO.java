package br.com.vemser.facetoface.dto;

import lombok.*;

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
