package br.com.vemser.facetoface.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FotoDTO {

    @Schema(description = "Foto da pessoa.")
    private byte[] data;

    private MultipartFile file;

    private String email;
}
