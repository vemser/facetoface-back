package br.com.vemser.facetoface.repository;


import br.com.vemser.facetoface.entity.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrilhaRepository extends JpaRepository<TrilhaEntity,Integer> {

}
