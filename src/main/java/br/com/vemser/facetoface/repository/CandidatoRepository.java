package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Integer> {
    Optional<CandidatoEntity> findByEmail(String email);

    @Query(" select new br.com.vemser.facetoface.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO(" +
            " c.idCandidato," +
            " c.nomeCompleto," +
            " c.email," +
            " c.notaProva," +
            " t.nome," +
            " c.edicao.nome)" +
            "  from CANDIDATO c " +
            " left join c.trilha t" +
            " where (:nomeCompleto is null or c.nomeCompleto = :nomeCompleto)" +
            " and (:nomeEdicao is null or c.edicao.nome = :nomeEdicao)" +
            " and (:nomeTrilha is null or c.trilha.nome = :nomeTrilha)")
    Page<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, String nomeTrilha, String nomeEdicao, Pageable pageable);
}
