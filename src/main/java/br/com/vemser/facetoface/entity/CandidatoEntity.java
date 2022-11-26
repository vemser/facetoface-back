package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Edicao;
import br.com.vemser.facetoface.entity.enums.Linguagem;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "CANDIDATO")
public class CandidatoEntity extends PessoaEntity{
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTAO_CREDITO")
//    @SequenceGenerator(name = "SEQ_CARTAO_CREDITO", sequenceName = "seq_cartao_credito", allocationSize = 1)
    @Column(name = "id_candidato")
    private Integer idCandidato;

    @Column(name = "id_edicao")
    @Enumerated(EnumType.ORDINAL)
    private Edicao edicao;

    @Column(name = "nota_prova")
    private Double notaProva;

    private List<Linguagem> linguagens;

    @Column(name = "observacoes")
    private String observacoes;

    @OneToOne(mappedBy = "candidatoEntity")
    private EntrevistaEntity entrevistaEntity;
}
