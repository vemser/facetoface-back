package br.com.vemser.facetoface.entity;

import br.com.vemser.facetoface.entity.enums.Legenda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrevistaEntity {
    private CandidatoEntity candidatoEntity;
    private UsuarioEntity usuarioEntity;
    private LocalDateTime dataEntrevista;
    private String observacoes;
    private Legenda legenda;
}
