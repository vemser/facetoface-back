package br.com.vemser.facetoface.entity.enums;

public enum Legenda {
    CONFIRMADA(1),
    PENDENTE(2),
    CANCELADA(3),
    OUTROS(4);

    private Integer legenda;

    Legenda(Integer legenda) {
        this.legenda = legenda;
    }

    public Integer getLegenda() {
        return legenda;
    }
}
