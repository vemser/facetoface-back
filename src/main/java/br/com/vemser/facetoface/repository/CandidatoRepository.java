package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity,Integer> {
    Optional<CandidatoEntity> findByEmail(String email);
    Page<CandidatoEntity> findAllByNomeCompleto(String nomeCompleto, Pageable pageable);

    Optional<CandidatoEntity> findByNomeCompleto(String nomeCompleto);

}
