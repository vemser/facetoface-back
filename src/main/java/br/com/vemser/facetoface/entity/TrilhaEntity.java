package br.com.vemser.facetoface.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TrilhaEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRILHA")
    @SequenceGenerator(name = "SEQ_TRILHA", sequenceName = "SEQ_TRILHA", allocationSize = 1)
    private Integer idTrilha;

    private String nome;
}
