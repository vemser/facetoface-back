package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Integer> {
}
