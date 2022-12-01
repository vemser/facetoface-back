package br.com.vemser.facetoface.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSenhaDTO {
    @NotNull
    @Schema(description = "senha do funcionário.", example = "123")
    private String senha;
    private String token;

}
