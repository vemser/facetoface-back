package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity,Integer> {

//    List<EntrevistaEntity> findAllByDataEntrevista_Month();

    Page<EntrevistaEntity> findByUsuarioEntity(UsuarioEntity usuarioEntity, Pageable pageable);

//    Page<EntrevistaEntity> findAllByDataEntrevista_Month(Month mes);

}
