package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity,Integer> {
    Optional<CandidatoEntity> findByEmail(String email);
    Page<CandidatoEntity> findByNomeCompleto(String nomeCompleto, Pageable pageable);

    @Query("select c from CANDIDATO c where c.nomeCompleto = :nome")
    Optional<CandidatoEntity> findByNome(String nome);

}
