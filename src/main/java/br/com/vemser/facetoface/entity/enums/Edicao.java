package br.com.vemser.facetoface.entity.enums;

public enum Edicao {
    PRIMEIRA(1),
    SEGUNDA(2),
    TERCEIRA(3),
    QUARTA(4),
    QUINTA(5),
    SEXTA(6),
    SETIMA(7),
    OITAVA(8),
    NONA(9),
    DECIMA(10),
    DECIMA_PRIMEIRA(11);

    private Integer edicao;

    Edicao(Integer edicao) {
        this.edicao = edicao;
    }

    public Integer getEdicao() {
        return edicao;
    }
}
