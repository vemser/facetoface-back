package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Integer> {
    Optional<ImageEntity> findByCandidato(CandidatoEntity candidatoEntity);
}
