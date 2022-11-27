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
@Entity(name = "TRILHA")
public class TrilhaEntity {

    @Id
    @Column(name = "ID_TRILHA")
    private Integer idTrilha;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trilha")
    private Set<UsuarioEntity> usuarioEntities;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trilha")
    private Set<CandidatoEntity> candidatoEntities;
}
