package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Integer> {
}
