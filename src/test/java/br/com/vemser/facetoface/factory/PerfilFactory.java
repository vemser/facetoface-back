package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.perfil.PerfilDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;

import java.util.Collections;

public class PerfilFactory {
    public static PerfilEntity getPerfilEntity() {
        return new PerfilEntity(2,
                "ADMIN",
                Collections.emptySet()
        );
    }

    public static PerfilDTO getPerfilDTO() {
        return new PerfilDTO("ADMIN");
    }
}
