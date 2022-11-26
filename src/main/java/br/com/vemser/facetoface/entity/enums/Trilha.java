package br.com.vemser.facetoface.entity.enums;

public enum Trilha {
    BACKEND(1),
    FRONTEND(2),
    QA(3),
    COLABORADOR(4);

    private Integer trilha;

    Trilha(Integer trilha) {
        this.trilha = trilha;
    }

    public Integer getTrilha() {
        return trilha;
    }
}
