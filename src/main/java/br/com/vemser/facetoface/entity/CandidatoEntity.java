package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Edicao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoEntity extends PessoaEntity{
    private Edicao edicao;
    private Double notaProva;
    private List<String> linguagens;
    private String observacoes;
}
