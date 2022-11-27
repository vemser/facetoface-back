package br.com.vemser.facetoface.entity.enums;

public enum Legenda {
    CONFIRMADA(0),
    PENDENTE(1),
    CANCELADA(2),
    OUTROS(3);

    private Integer legenda;

    Legenda(Integer legenda) {
        this.legenda = legenda;
    }

    public Integer getLegenda() {
        return legenda;
    }
}
