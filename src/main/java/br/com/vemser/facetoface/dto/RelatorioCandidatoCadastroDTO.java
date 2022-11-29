package br.com.vemser.facetoface.dto;

import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelatorioCandidatoCadastroDTO {
    private Integer idCandidato;
    private String nomeCompleto;
    private String email;
    private Double notaProva;
    private String trilha;
    private String edicao;
    private Genero genero;
    private char ativo;
    private String estado;
    private String cidade;
    private String linguagens;
    private String observacoes;
    @Lob
    private byte[] dado;

}
