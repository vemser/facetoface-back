package br.com.vemser.facetoface.entity;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EDICAO", referencedColumnName = "ID_EDICAO")
    @ToString.Exclude
    private EdicaoEntity edicao;

    @Column(name = "nota_prova")
    private Double notaProva;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CANDIDATO_LINGUAGEM",
            joinColumns = @JoinColumn(name = "ID_CANDIDATO"),
            inverseJoinColumns = @JoinColumn(name = "ID_LINGUAGEM"))
    private List<LinguagemEntity> linguagens;

    @Column(name = "observacoes")
    private String observacoes;

    @OneToOne(mappedBy = "candidatoEntity")
    private EntrevistaEntity entrevistaEntity;

    @OneToOne(mappedBy = "candidato")
    private ImageEntity imageEntity;

    @OneToOne(mappedBy = "candidato")
    private CurriculoEntity curriculoEntity;
}
