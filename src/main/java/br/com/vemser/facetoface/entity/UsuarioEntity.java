package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity extends PessoaEntity{
    private Perfil perfil;
    private String senha;
}
