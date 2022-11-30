package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Genero;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "CANDIDATO")
public class CandidatoEntity{
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

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trilha", referencedColumnName = "id_trilha")
    private TrilhaEntity trilha;

    @Column(name = "ativo")
    private char ativo;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CANDIDATO_LINGUAGEM",
            joinColumns = @JoinColumn(name = "ID_CANDIDATO"),
            inverseJoinColumns = @JoinColumn(name = "ID_LINGUAGEM"))
    private Set<LinguagemEntity> linguagens;

    @Column(name = "observacoes")
    private String observacoes;

    @OneToOne(mappedBy = "candidatoEntity")
    private EntrevistaEntity entrevistaEntity;

    @OneToOne(mappedBy = "candidato")
    private ImageEntity imageEntity;

    @OneToOne(mappedBy = "candidato")
    private CurriculoEntity curriculoEntity;

    public boolean isEnabled() {
        {
            return getAtivo() == 'T';
        }
    }
}
