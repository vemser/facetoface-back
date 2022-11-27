package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurriculoRepository extends JpaRepository<CurriculoEntity, Integer> {
    Optional<CurriculoEntity> findByCandidato(CandidatoEntity candidatoEntity);
}
