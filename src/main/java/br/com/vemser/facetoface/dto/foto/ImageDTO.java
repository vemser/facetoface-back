package br.com.vemser.facetoface.dto.foto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ImageDTO {

    @Schema(example = "foto")
    String image;

}
