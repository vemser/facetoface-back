package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.entity.ImageEntity;

import java.util.HexFormat;

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoEntity;

public class ImageFactory {
    public static ImageEntity getImageEntity() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setCandidato(getCandidatoEntity());
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        imageEntity.setData(bytes);
        imageEntity.setIdImagem(1);
        imageEntity.setTipo("png");
        return imageEntity;
    }

    public static ImageEntity getImageUsuario() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUsuario(UsuarioFactory.getUsuarioEntity());
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        imageEntity.setData(bytes);
        imageEntity.setIdImagem(1);
        imageEntity.setTipo("png");
        return imageEntity;
    }
}
