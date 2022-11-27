package br.com.vemser.facetoface.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FotoDTO {

    @Schema(description = "Foto da pessoa.")
    private byte[] data;
}
