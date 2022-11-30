package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.PerfilDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        usuarioEntity.setEmail("julio.gabriel@dbccompany.com.br");
        Set<PerfilEntity> perfil = new HashSet<>();
        perfil.add(PerfilFactory.getPerfilEntity());
        usuarioEntity.setPerfis(perfil);
        return usuarioEntity;
    }

    public static UsuarioDTO getUsuarioDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1);
        usuarioDTO.setNomeCompleto("Débora Sophia da Silva");
        usuarioDTO.setEmail("julio.gabriel@dbccompany.com.br");
        usuarioDTO.setGenero(Genero.FEMININO);
        usuarioDTO.setCidade("Mossoró");
        usuarioDTO.setEstado("RN");
        usuarioDTO.setAtivo('T');
        usuarioDTO.setTrilha(TrilhaFactory.getTrilhaDTO());
        List<PerfilDTO> perfis = new ArrayList<>();
        perfis.add(PerfilFactory.getPerfilDTO());
        usuarioDTO.setPerfis(perfis);

        return usuarioDTO;
    }
}
