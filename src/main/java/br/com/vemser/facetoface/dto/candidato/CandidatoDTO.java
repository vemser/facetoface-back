package br.com.vemser.facetoface.dto.candidato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO extends CandidatoCreateDTO{
    private Integer idCandidato;
}
