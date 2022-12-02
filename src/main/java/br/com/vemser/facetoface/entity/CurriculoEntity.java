package br.com.vemser.facetoface.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Curriculo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CURRICULO")
    @SequenceGenerator(name = "SEQ_CURRICULO", sequenceName = "seq_curriculo", allocationSize = 1)
    @Column(name = "ID_CURRICULO")
    private Integer idCurriculo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dado")
    @Lob
    private byte[] dado;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CANDIDATO", referencedColumnName = "ID_CANDIDATO")
    private CandidatoEntity candidato;
}
