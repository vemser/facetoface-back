package br.com.vemser.facetoface.entity.enums;

public enum Genero {
    MASCULINO(1),
    FEMININO(2),
    OUTRO(3);

    private Integer genero;

    Genero(Integer genero) {
        this.genero = genero;
    }

    public Integer getGenero() {
        return genero;
    }
}
