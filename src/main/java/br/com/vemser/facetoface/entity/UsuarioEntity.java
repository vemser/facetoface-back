package br.com.vemser.facetoface.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "USUARIO")
public class UsuarioEntity extends PessoaEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_ID_USUARIO", allocationSize = 1)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USUARIO_PERFIL",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_PERFIL"))
    private Set<PerfilEntity> perfis;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioEntity")
    private Set<EntrevistaEntity> entrevistas;

    @Column(name = "senha")
    private String senha;
}
