package br.com.vemser.facetoface.dto.recuperarsenhadto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecuperarSenhaDTO {

        @NotNull
        private String email;

}
