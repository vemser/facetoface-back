package br.com.vemser.facetoface.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "FOTOS")
public class ImageEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FOTOS")
    @SequenceGenerator(name = "SEQ_FOTOS", sequenceName = "SEQ_FOTOS", allocationSize = 1)
    @Column(name="ID_FOTOS")
    private Integer idImagem;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dado")
    @Lob
    private byte[] data;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "imageEntity")
    private UsuarioEntity usuario;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CANDIDATO", referencedColumnName = "ID_CANDIDATO")
    private CandidatoEntity candidato;
}
