package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.entity.LinguagemEntity;

public class LinguagemFactory {
        public static LinguagemEntity getLinguagemEntity() {
        LinguagemEntity linguagemEntity = new LinguagemEntity();
        linguagemEntity.setIdLinguagem(1);
        linguagemEntity.setNome("Java");

        return linguagemEntity;
    }

        private static LinguagemDTO getLinguagemDTO() {
                LinguagemDTO linguagemDTO = new LinguagemDTO();
                linguagemDTO.setNome("Java");
                return linguagemDTO;
        }
}
