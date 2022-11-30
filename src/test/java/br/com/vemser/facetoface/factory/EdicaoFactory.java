package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;

public class EdicaoFactory {

    public static EdicaoEntity getEdicaoEntity() {
        EdicaoEntity edicao = new EdicaoEntity();
        edicao.setIdEdicao(1);
        edicao.setNome("Edição 10");

        return edicao;
    }

    public static EdicaoDTO getEdicaoDTO() {
        return new EdicaoDTO("Edição 10");
    }
}
