package br.com.vemser.facetoface.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "EDICAO")
public class Edicao {
    @Id
    @Column(name = "ID_EDICAO")
    private Integer idEdicao;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "edicao")
    private Set<CandidatoEntity> candidatoEntities;
}
