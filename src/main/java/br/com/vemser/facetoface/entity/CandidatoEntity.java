package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Edicao;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CANDIDATO")
    @SequenceGenerator(name = "SEQ_CANDIDATO", sequenceName = "SEQ_ID_CANDIDATO", allocationSize = 1)
    @Column(name = "id_candidato")
    private Integer idCandidato;

    @Column(name = "id_edicao")
    @Enumerated(EnumType.ORDINAL)
    private Edicao edicao;

    @Column(name = "nota_prova")
    private Double notaProva;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CANDIDATO_LINGUAGEM",
            joinColumns = @JoinColumn(name = "ID_CANDIDATO"),
            inverseJoinColumns = @JoinColumn(name = "ID_LINGUAGEM"))
    private List<Linguagem> linguagens;

    @Column(name = "observacoes")
    private String observacoes;

    @OneToOne(mappedBy = "candidatoEntity")
    private EntrevistaEntity entrevistaEntity;

    @Lob
    @Column(name = "curriculo")
    private byte[] curriculo;
}
