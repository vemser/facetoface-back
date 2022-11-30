package br.com.vemser.facetoface.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEmails {
    CONFIRMACAO("Token para confirmar a entrevista"),
    REC_SENHA("Token para recuperação do e-mail realizada!");

    private final String descricao;


}