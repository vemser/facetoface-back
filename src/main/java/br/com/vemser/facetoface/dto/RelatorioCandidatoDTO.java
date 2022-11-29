package br.com.vemser.facetoface.dto;

import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelatorioCandidatoDTO {
    private Integer idCandidato;
    private String nomeCompleto;
    private String email;
    private EdicaoEntity edicao;
    private Double notaProva;
    private Genero genero;
    private String trilha;
    private char ativo;
    private String observacoes;

}
