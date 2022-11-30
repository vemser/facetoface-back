package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;

import static br.com.vemser.facetoface.factory.TrilhaFactory.getTrilhaEntity;

public class UsuarioFactory {
    public static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setSenha("123");
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setGenero(Genero.MASCULINO);
        usuarioEntity.setTrilha(getTrilhaEntity());
        usuarioEntity.setAtivo('T');
        usuarioEntity.setNomeCompleto("Heloise Isabela Lopes");
        usuarioEntity.setCidade("Santana");
        usuarioEntity.setEstado("AP");
        usuarioEntity.setEmail("heloise.lopes@dbccompany.com.br");
        return usuarioEntity;
    }
}
