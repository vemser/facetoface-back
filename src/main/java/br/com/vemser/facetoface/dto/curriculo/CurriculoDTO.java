package br.com.vemser.facetoface.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurriculoDTO {

    @Schema(description = "Curriculo do candidato.")
    private byte[] data;
}
