package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    Page<UsuarioEntity> findByNomeCompleto(String nomeCompleto, Pageable pageable);

    @Query("SELECT u FROM USUARIO u WHERE u.nomeCompleto  = :nome")
    Optional<UsuarioEntity> findByNome(String nome);
}
