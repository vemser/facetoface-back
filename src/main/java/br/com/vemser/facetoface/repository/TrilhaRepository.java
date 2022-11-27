package br.com.vemser.facetoface.repository;


import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrilhaRepository extends JpaRepository<TrilhaEntity,Integer> {
    Optional<TrilhaEntity> findByNome(String nome);

}
