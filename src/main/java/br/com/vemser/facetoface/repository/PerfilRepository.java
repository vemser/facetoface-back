package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilEntity, Integer> {
    Optional<PerfilEntity> findByNome(String nome);
}
