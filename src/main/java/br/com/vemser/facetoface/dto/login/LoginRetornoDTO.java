package br.com.vemser.facetoface.dto.login;

import br.com.vemser.facetoface.dto.PerfilDTO;
import lombok.Data;

import java.util.List;

@Data
public class LoginRetornoDTO {
    private String email;
    private List<PerfilDTO> perfis;
}
