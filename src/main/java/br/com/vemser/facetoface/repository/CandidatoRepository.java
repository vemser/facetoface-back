package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity,Integer> {
    Optional<CandidatoEntity> findByEmail(String email);
    Optional<CandidatoEntity> findByNomeCompleto(String nomeCompleto);

}
