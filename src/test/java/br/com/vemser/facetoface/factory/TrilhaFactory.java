package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.entity.TrilhaEntity;

public class TrilhaFactory {
    public static TrilhaEntity getTrilhaEntity() {
        TrilhaEntity trilha = new TrilhaEntity();
        trilha.setIdTrilha(1);
        trilha.setNome("BACKEND");

        return trilha;
    }

    public static TrilhaDTO getTrilhaDTO() {
        return new TrilhaDTO("BACKEND");
    }
}
