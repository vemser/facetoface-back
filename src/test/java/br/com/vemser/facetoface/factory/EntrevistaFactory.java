package br.com.vemser.facetoface.factory;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoDTO;
import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoEntity;
import static br.com.vemser.facetoface.factory.UsuarioFactory.getUsuarioDTO;

public class EntrevistaFactory {
    public static EntrevistaEntity getEntrevistaEntity() {
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setIdEntrevista(1);
        entrevistaEntity.setDataEntrevista(LocalDateTime.of(LocalDate.now().plusDays(1),
                LocalTime.of(15, 0)));
        entrevistaEntity.setCidade("Santana");
        entrevistaEntity.setEstado("AP");
        entrevistaEntity.setObservacoes("Sem observações.");
        entrevistaEntity.setLegenda(Legenda.PENDENTE);
        entrevistaEntity.setCandidatoEntity(getCandidatoEntity());

        return entrevistaEntity;
    }

    public static EntrevistaDTO getEntrevistaDTO() {
        CandidatoDTO candidatoDTO = getCandidatoDTO();
        UsuarioDTO usuarioDTO = getUsuarioDTO();

        EntrevistaDTO entrevistaDTO = new EntrevistaDTO();
        entrevistaDTO.setIdEntrevista(1);
        entrevistaDTO.setCandidatoDTO(candidatoDTO);
        entrevistaDTO.setUsuarioDTO(usuarioDTO);
        entrevistaDTO.setUsuarioEmail(usuarioDTO.getEmail());
        entrevistaDTO.setCandidatoEmail(candidatoDTO.getEmail());

        return entrevistaDTO;
    }

    public static EntrevistaAtualizacaoDTO getEntrevistaAtualizacaoDTO() {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO =
                new EntrevistaAtualizacaoDTO(
                        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)),
                        "Santana",
                        "AP",
                        "Sem observações",
                        "julio.gabriel@dbccompany.com.br"
                );

        return entrevistaAtualizacaoDTO;
    }
}
