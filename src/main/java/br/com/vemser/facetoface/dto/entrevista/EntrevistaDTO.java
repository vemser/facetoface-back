package br.com.vemser.facetoface.dto.entrevista;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrevistaDTO extends EntrevistaCreateDTO{

    private Integer idEntrevista;

    private CandidatoDTO candidatoDTO;

    private UsuarioDTO usuarioDTO;
}
