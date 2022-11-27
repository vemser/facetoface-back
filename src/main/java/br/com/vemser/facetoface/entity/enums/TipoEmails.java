package br.com.vemser.facetoface.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEmails {

//    CREATE("Cadastro realizado com sucesso!"),
//    UPDATE("Alteração de Dados Cadastrais!"),
//    DELETE("Acesso da conta encerrado!"),
    CONFIRMACAO("Token para confirmar a entrevista"),
    REC_SENHA("Token para recuperação do e-mail realizada!");

    private final String descricao;


}