package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;

import java.util.HexFormat;

public class CurriculoFactory {
    public static CurriculoEntity getCurriculoEntity(){
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        return new CurriculoEntity(2,
                "curriculo",
                "png",
                bytes,
                candidatoEntity);
    }
}
