package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Genero;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "USUARIO")
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_ID_USUARIO", allocationSize = 1)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

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
    @JoinTable(name = "USUARIO_PERFIL",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_PERFIL"))
    private List<PerfilEntity> perfis;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioEntity")
    private Set<EntrevistaEntity> entrevistas;

    @Column(name = "senha")
    private String senha;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private ImageEntity imageEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        {
            return getAtivo() == 'T';
        }
    }
}
