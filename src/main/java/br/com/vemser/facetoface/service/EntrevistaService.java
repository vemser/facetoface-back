package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.candidato.LegendaDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EntrevistaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;

    private final CandidatoService candidatoService;

    private final UsuarioService usuarioService;

    private final ObjectMapper objectMapper;

    public EntrevistaEntity findById(Integer id) throws RegraDeNegocioException {
        return entrevistaRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("Entrevista não encontrada!"));
    }

    public EntrevistaDTO converterParaEntrevistaDTO(EntrevistaEntity entrevistaEntity) {
        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaEntity, EntrevistaDTO.class);
        usuarioService.converterEmDTO(entrevistaEntity.getUsuarioEntity());
        candidatoService.converterEmDTO(entrevistaEntity.getCandidatoEntity());
        return entrevistaDTO;
    }

    public PageDTO<EntrevistaDTO> list(Integer pagina, Integer tamanho){
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAll(pageRequest);
        List<EntrevistaDTO> entrevistaDTOList = entrevistaRepository.findAll().stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntityPage.getTotalElements(),
                entrevistaEntityPage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
    }

    public EntrevistaDTO createEntrevista(EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuario = usuarioService.findByNome(entrevistaCreateDTO.getUsuarioNome());
        CandidatoEntity candidato = candidatoService.findByNome(entrevistaCreateDTO.getCandidatoNome());
        if(entrevistaRepository.findByCandidatoEntity(candidato).isPresent()){
            throw new RegraDeNegocioException("Entrevista para o Candidato já agendada!");
        }
        String cidade = entrevistaCreateDTO.getCidade();
        String estado = entrevistaCreateDTO.getEstado();
        String observacoes = entrevistaCreateDTO.getObservacoes();
        LocalDateTime dia = entrevistaCreateDTO.getDataEntrevista();
        if(entrevistaRepository.findByDataEntrevista(dia).isPresent()){
            throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
        }
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setDataEntrevista(dia);
        entrevistaEntity.setCandidatoEntity(candidato);
        entrevistaEntity.setUsuarioEntity(usuario);
        entrevistaEntity.setCidade(cidade);
        entrevistaEntity.setEstado(estado);
        entrevistaEntity.setObservacoes(observacoes);
        entrevistaEntity.setLegenda(Legenda.PENDENTE);

        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevistaEntity);
        //enviar o email aqui para confirmar

        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaSalva, EntrevistaDTO.class);
        return entrevistaDTO;
    }

    public void deletarEntrevista(Integer idEntrevista) throws RegraDeNegocioException {
        EntrevistaEntity entrevistaRecuperada = findById(idEntrevista);
        entrevistaRepository.delete(entrevistaRecuperada);
    }

    public EntrevistaDTO atualizarEntrevista(Integer idEntrevista, EntrevistaAtualizacaoDTO entrevistaCreateDTO, Legenda legenda) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = findById(idEntrevista);
        if(entrevistaRepository.findByDataEntrevista(entrevistaCreateDTO.getDataEntrevista()).isPresent()){
            throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
        }
        entrevista.setCidade(entrevistaCreateDTO.getCidade());
        entrevista.setEstado(entrevistaCreateDTO.getEstado());
        entrevista.setObservacoes(entrevistaCreateDTO.getObservacoes());
        entrevista.setDataEntrevista(entrevistaCreateDTO.getDataEntrevista());
        entrevista.setLegenda(legenda);
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevista);
        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaSalva, EntrevistaDTO.class);
        return entrevistaDTO;
    }
}