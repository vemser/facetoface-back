package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Integer> {
}
