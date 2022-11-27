package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Integer> {

    @Query("SELECT u FROM USUARIO u WHERE u.email = :email")
    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByNomeCompleto(String nomeCompleto);

}
