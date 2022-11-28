package br.com.vemser.facetoface.dto.entrevista;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.candidato.LegendaDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EntrevistaCreateDTO {

    @NotNull
    @Schema(description = "Candidato que irá participar da entrevista.")
    private String candidatoNome;

    @NotNull
    @Schema(description = "Usuário que irá realizar a entrevista.")
    private String usuarioNome;

    @NotNull
    @Schema(description = "Dia/Mês/Ano que irá ocorrer a entrevista.")
    private LocalDate diaMesAno;

    @NotNull
    @Schema(description = "Cidade em que o usuário irá realizar a entrevista")
    private String cidade;

    @NotNull
    @Schema(description = "Estado em que o usuário irá realizar a entrevista")
    private String estado;

    @NotNull
    @Schema(description = "Hora/Min/Seg que irá ocorrer a entrevista.")
    private LocalTime horasMin;

    @NotNull
    @Schema(description = "Observações referentes a entrevista.")
    private String observacoes;

    @NotNull
    @Schema(description = "Legenda referente a entrevista.")
    private LegendaDTO legenda;
}
