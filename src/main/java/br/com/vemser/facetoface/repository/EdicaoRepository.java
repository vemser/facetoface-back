package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.EdicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdicaoRepository extends JpaRepository<EdicaoEntity,Integer> {
    Optional<EdicaoEntity> findByNome(String nome);


}
