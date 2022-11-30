package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.enums.Genero;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static br.com.vemser.facetoface.factory.EdicaoFactory.getEdicaoDTO;
import static br.com.vemser.facetoface.factory.TrilhaFactory.getTrilhaDTO;

public class CandidatoFactory {
        public static CandidatoEntity getCandidatoEntity() {
        LinguagemEntity linguagemEntity = LinguagemFactory.getLinguagemEntity();
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        linguagemList.add(linguagemEntity);

        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNotaProva(8.00);
        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setGenero(Genero.FEMININO);
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        candidatoEntity.setEdicao(EdicaoFactory.getEdicaoEntity());
        candidatoEntity.setTrilha(TrilhaFactory.getTrilhaEntity());
        candidatoEntity.setAtivo('T');

        return candidatoEntity;
    }

        public static CandidatoCreateDTO getCandidatoCreateDTO() {
        LinguagemDTO linguagemDTO = new LinguagemDTO("Java");
        List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
        linguagemDTOList.add(linguagemDTO);

        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
        candidatoCreateDTO.setNomeCompleto("Heloise Isabela Lopes");
        candidatoCreateDTO.setCidade("Santana");
        candidatoCreateDTO.setEstado("AP");
        candidatoCreateDTO.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoCreateDTO.setLinguagens(new HashSet<>(linguagemDTOList));
        candidatoCreateDTO.setTrilha(getTrilhaDTO());
        candidatoCreateDTO.setEdicao(getEdicaoDTO());
//        candidatoCreateDTO.setGenero(Genero.FEMININO);
        candidatoCreateDTO.setAtivo('T');

        return candidatoCreateDTO;
    }
}
