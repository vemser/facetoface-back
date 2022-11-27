package br.com.vemser.facetoface.entity.enums;

public enum Genero {
    MASCULINO(0),
    FEMININO(1),
    OUTRO(2);

    private Integer genero;

    Genero(Integer genero) {
        this.genero = genero;
    }

    public Integer getGenero() {
        return genero;
    }
}
