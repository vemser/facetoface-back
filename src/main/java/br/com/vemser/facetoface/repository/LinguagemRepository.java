package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.LinguagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinguagemRepository extends JpaRepository<LinguagemEntity, Integer> {
    Optional<LinguagemEntity> findByNome(String nome);
}
