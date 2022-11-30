package br.com.vemser.facetoface.repository;

import br.com.vemser.facetoface.dto.RelatorioCandidatoCadastroDTO;
import br.com.vemser.facetoface.dto.RelatorioCandidatoPaginaPrincipalDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Lob;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity,Integer> {
    Optional<CandidatoEntity> findByEmail(String email);
    Page<CandidatoEntity> findAllByNomeCompleto(String nomeCompleto, Pageable pageable);

    Optional<CandidatoEntity> findByNomeCompleto(String nomeCompleto);

    @Query(" select new br.com.vemser.facetoface.dto.RelatorioCandidatoCadastroDTO(" +
            " c.idCandidato," +
            " c.nomeCompleto," +
            " c.email," +
            " c.notaProva," +
            " t.nome," +
            " e.nome," +
            " c.genero," +
            " c.ativo," +
            " c.estado," +
            " c.cidade," +
            " c.observacoes," +
            " c.curriculoEntity.dado)" +
            "  from CANDIDATO c " +
            " left join c.trilha t" +
            " left join c.edicao e" +
            " where (:nomeCompleto is null or c.nomeCompleto = :nomeCompleto)" +
            " and (:nomeTrilha is null or c.trilha.nome = :nomeTrilha)")
    Page<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(String nomeCompleto, String nomeTrilha, Pageable pageable);

    @Query(" select new br.com.vemser.facetoface.dto.RelatorioCandidatoPaginaPrincipalDTO(" +
            " c.idCandidato," +
            " c.nomeCompleto," +
            " c.email," +
            " c.notaProva," +
            " t.nome)" +
            "  from CANDIDATO c " +
            " left join c.trilha t" +
            " where (:nomeCompleto is null or c.nomeCompleto = :nomeCompleto)" +
            " and (:nomeTrilha is null or c.trilha.nome = :nomeTrilha)")
    Page<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, String nomeTrilha, Pageable pageable);
}
