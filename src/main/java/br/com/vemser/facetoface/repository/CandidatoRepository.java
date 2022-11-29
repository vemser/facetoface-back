package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.dto.RelatorioCandidatoDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity,Integer> {
    Optional<CandidatoEntity> findByEmail(String email);
    Page<CandidatoEntity> findAllByNomeCompleto(String nomeCompleto, Pageable pageable);

    Optional<CandidatoEntity> findByNomeCompleto(String nomeCompleto);

    @Query(" select new br.com.vemser.facetoface.dto.RelatorioCandidatoDTO(" +
            " c.idCandidato," +
            " c.nomeCompleto," +
            " c.email," +
            " c.edicao," +
            " c.notaProva," +
            " c.genero," +
            " t.nome," +
            " c.ativo," +
            " c.observacoes)" +
            "  from CANDIDATO c " +
            " left join c.trilha t" +
            " where (:nomeCompleto is null or c.nomeCompleto = :nomeCompleto)" +
            " and (:trilhaEntity is null or c.trilha = :trilhaEntity)")
    Page<RelatorioCandidatoDTO> listarRelatoriosLocacao(String nomeCompleto, TrilhaEntity trilhaEntity, Pageable pageable);
}
