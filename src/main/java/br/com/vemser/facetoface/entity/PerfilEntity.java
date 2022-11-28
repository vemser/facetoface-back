package br.com.vemser.facetoface.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "PERFIL")
public class PerfilEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERFIL")
    @SequenceGenerator(name = "SEQ_PERFIL", sequenceName = "SEQ_PERFIL", allocationSize = 1)
    @Column(name = "ID_PERFIL")
    private Integer idPerfil;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "perfis")
//    @JoinTable(
//            name = "USUARIO_PERFIL",
//            joinColumns = @JoinColumn(name = "ID_PERFIL"),
//            inverseJoinColumns = @JoinColumn(name = "ID_USUARIO")
//    )
    private Set<UsuarioEntity> usuarios;

    @Override
    public String getAuthority() {
        return nome;
    }
}

