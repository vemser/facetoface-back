package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.EntrevistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity,Integer> {
}
