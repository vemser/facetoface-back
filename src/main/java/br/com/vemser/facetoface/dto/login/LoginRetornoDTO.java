package br.com.vemser.facetoface.dto.login;

import br.com.vemser.facetoface.dto.perfil.PerfilDTO;
import lombok.Data;

import java.util.List;

@Data
public class LoginRetornoDTO {
    private String email;
    private String nomeCompleto;
    private List<PerfilDTO> perfis;
}
