package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
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

    public PageDTO<EntrevistaDTO> listarPorMes(Integer pagina, Integer tamanho, Month mes){
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntitiePage = entrevistaRepository.findAll(pageRequest);
        List<EntrevistaDTO> entrevistaDTOList = entrevistaRepository.findAllByDataEntrevista_Month(mes).stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntitiePage.getTotalElements(),
                entrevistaEntitiePage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
    }


    public PageDTO<EntrevistaDTO> listarPorUsuario(Integer pagina, Integer tamanho, String nome) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        UsuarioEntity usuarioEntity = usuarioService
    }


    public EntrevistaDTO createEntrevista(EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuario = usuarioService.findById(entrevistaCreateDTO.getUsuarioDTO().getIdUsuario());
        CandidatoEntity candidato = candidatoService.findById(entrevistaCreateDTO.getCandidatoDTO().getIdCandidato());
        String cidade = entrevistaCreateDTO.getCidade();
        String estado = entrevistaCreateDTO.getEstado();
        String observacoes = entrevistaCreateDTO.getObservacoes();
        LocalDate dia = entrevistaCreateDTO.getDiaMesAno();
        LocalTime hora = entrevistaCreateDTO.getHorasMin();
        LocalDateTime dataReal = LocalDateTime.of(dia, hora);
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setDataEntrevista(dataReal);
        entrevistaEntity.setCandidatoEntity(candidato);
        entrevistaEntity.setUsuarioEntity(usuario);
        entrevistaEntity.setCidade(cidade);
        entrevistaEntity.setEstado(estado);
        entrevistaEntity.setObservacoes(observacoes);
        entrevistaEntity.setLegenda(entrevistaEntity.getLegenda());

        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevistaEntity);
        //enviar o email aqui

        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaSalva, EntrevistaDTO.class);
        return entrevistaDTO;
    }

    public void deletarEntrevista(Integer idEntrevista) throws RegraDeNegocioException {
        EntrevistaEntity entrevistaRecuperada = findById(idEntrevista);
        entrevistaRepository.delete(entrevistaRecuperada);
    }

    public EntrevistaDTO atualizarEntrevista(Integer idEntrevista, EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = findById(idEntrevista);
        entrevista.setDataEntrevista(LocalDateTime.of(entrevistaCreateDTO.getDiaMesAno(), entrevistaCreateDTO.getHorasMin()));
        entrevista.setCidade(entrevistaCreateDTO.getCidade());
        entrevista.setEstado(entrevistaCreateDTO.getEstado());
        entrevista.setObservacoes(entrevistaCreateDTO.getObservacoes());
        entrevista.setUsuarioEntity(usuarioService.findById(entrevistaCreateDTO.getUsuarioDTO().getIdUsuario()));
        entrevista.setCandidatoEntity(candidatoService.findById(entrevistaCreateDTO.getCandidatoDTO().getIdCandidato()));
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevista);

        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaSalva, EntrevistaDTO.class);

        return entrevistaDTO;
   }

//    public PageDTO<EntrevistaDTO> list(Integer pagina, Integer tamanho){
//        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
//        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.findAll(pageRequest);
//        List<CandidatoDTO> candidatoDTOList = candidatoRepository.findAll().stream()
//                .map(this::converterEmDTO)
//                .toList();
//        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
//                candidatoEntityPage.getTotalPages(),
//                pagina,
//                tamanho,
//                candidatoDTOList);
    }



