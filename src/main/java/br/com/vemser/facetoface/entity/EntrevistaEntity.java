package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Legenda;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "ENTREVISTA")
public class EntrevistaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTREVISTA")
    @SequenceGenerator(name = "SEQ_ENTREVISTA", sequenceName = "SEQ_ID_ENTREVISTAS", allocationSize = 1)
    @Column(name = "id_entrevista")
    private Integer idEntrevista;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato", referencedColumnName = "id_candidato")
    private CandidatoEntity candidatoEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ToString.Exclude
    private UsuarioEntity usuarioEntity;

    @Column(name = "data_hora_entrevista")
    private Date dataEntrevista;

    @Column(name = "observacoes")
    private String observacoes;

    //verificar com os instrutores
    @Column(name = "cidade")
    private String cidade;

    //verificar com os instrutores
    @Column(name = "estado")
    private String estado;

    @Column(name = "id_legenda")
    @Enumerated(EnumType.ORDINAL)
    private Legenda legenda;
}
