package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity,Integer> {

    Optional<EntrevistaEntity> findByCandidatoEntity(CandidatoEntity candidatoEntity);

    List<EntrevistaEntity> findByDataEntrevista(LocalDateTime localDateTime);

    @Query("SELECT e FROM ENTREVISTAS e " +
            "WHERE EXTRACT (MONTH FROM e.dataEntrevista) = :mes AND EXTRACT (YEAR FROM e.dataEntrevista) = :ano" +
            " ORDER BY e.dataEntrevista ASC  ")
    Page<EntrevistaEntity> findAllByMes(Integer mes, Integer ano, PageRequest pageRequest);

}
