package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity,Integer> {

    Page<EntrevistaEntity> findByUsuarioEntity(UsuarioEntity usuarioEntity, Pageable pageable);

    Optional<EntrevistaEntity> findByCandidatoEntity(CandidatoEntity candidatoEntity);

    Optional<EntrevistaEntity> findByDataEntrevista(LocalDateTime localDateTime);

}
