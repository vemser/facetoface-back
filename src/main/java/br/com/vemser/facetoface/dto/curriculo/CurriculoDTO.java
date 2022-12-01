package br.com.vemser.facetoface.dto.curriculo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurriculoDTO {

    @Schema(description = "Curriculo do candidato.")
    private byte[] data;
}
