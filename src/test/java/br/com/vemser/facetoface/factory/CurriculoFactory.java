package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;

import java.util.HexFormat;

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoEntity;

public class CurriculoFactory {
    public static CurriculoEntity getCurriculoEntity() {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        return new CurriculoEntity(2,
                "curriculo",
                "png",
                bytes,
                candidatoEntity);
    }
}
