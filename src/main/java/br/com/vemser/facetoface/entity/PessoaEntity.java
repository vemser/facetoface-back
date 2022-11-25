package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.enums.Trilha;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PessoaEntity {
    private String nomeCompleto;
    private String cidade;
    private String estado;
    private String email;
    private Genero genero;
    private byte[] foto;
    private Trilha trilha;
}
