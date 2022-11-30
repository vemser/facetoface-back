package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;

import java.time.LocalDateTime;

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoDTO;
import static br.com.vemser.facetoface.factory.UsuarioFactory.getUsuarioDTO;

public class EntrevistaFactory {
    public static EntrevistaEntity getEntrevistaEntity() {
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setIdEntrevista(1);
        entrevistaEntity.setDataEntrevista(LocalDateTime.now().plusDays(1));
        entrevistaEntity.setCidade("Santana");
        entrevistaEntity.setEstado("AP");
        entrevistaEntity.setObservacoes("Sem observações.");
        entrevistaEntity.setLegenda(Legenda.PENDENTE);

        return entrevistaEntity;
    }

    public static EntrevistaDTO getEntrevistaDTO() {
        EntrevistaDTO entrevistaDTO = new EntrevistaDTO();
        entrevistaDTO.setIdEntrevista(1);
        entrevistaDTO.setCandidatoDTO(getCandidatoDTO());
        entrevistaDTO.setUsuarioDTO(getUsuarioDTO());

        return entrevistaDTO;
    }
}
