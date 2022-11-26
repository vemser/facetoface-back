package br.com.vemser.facetoface.dto.entrevista;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;

import java.time.LocalDateTime;

public class EntrevistaCreateDTO {
    private CandidatoEntity candidatoEntity;
    private UsuarioEntity usuarioEntity;
    private LocalDateTime dataEntrevista;
    private String observacoes;
    private Legenda legenda;
}
