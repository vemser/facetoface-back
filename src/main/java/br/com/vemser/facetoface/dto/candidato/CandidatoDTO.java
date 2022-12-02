package br.com.vemser.facetoface.dto.candidato;

import br.com.vemser.facetoface.entity.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO extends CandidatoCreateDTO {
    private Integer idCandidato;
    private Genero genero;
}
