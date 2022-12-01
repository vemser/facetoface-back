package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EntrevistaRepository;
import br.com.vemser.facetoface.security.TokenService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.vemser.facetoface.factory.EntrevistaFactory.getEntrevistaAtualizacaoDTO;
import static br.com.vemser.facetoface.factory.EntrevistaFactory.getEntrevistaDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntrevistaServiceTest {
    @InjectMocks
    private EntrevistaService entrevistaService;
    @Mock
    private EntrevistaRepository entrevistaRepository;
    @Mock
    private CandidatoService candidatoService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private EmailService emailService;
    @Mock
    private TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(entrevistaService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornarEntrevistaProcuradaPeloIdCorretamente() throws RegraDeNegocioException {
        final int idEntrevista = 1;
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(entrevistaRepository.findById(1)).thenReturn(Optional.of(entrevistaEntity));
        EntrevistaEntity entrevistaRetornada = entrevistaService.findById(1);

        assertEquals(idEntrevista, entrevistaRetornada.getIdEntrevista());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoIdNaoCadastradoNoBanco() throws RegraDeNegocioException {
        when(entrevistaRepository.findById(1)).thenReturn(Optional.empty());
        entrevistaService.findById(1);
    }

    @Test
    public void deveRetornarUmaListaDeEntrevistaDTO() {
        final int tamanhoEsperado = 1;
        final int pagina = 0;
        final int tamanho = 5;

        EntrevistaEntity entrevista = getEntrevistaEntity();
        PageImpl<EntrevistaEntity> page =
                new PageImpl<>(List.of(entrevista), PageRequest.of(pagina, tamanho), 0);

        when(entrevistaRepository.findAll(any(PageRequest.class))).thenReturn(page);

        PageDTO<EntrevistaDTO> entrevistaDTOS = entrevistaService.list(pagina, tamanho);

        assertEquals(pagina, entrevistaDTOS.getPagina());
        assertEquals(tamanho, entrevistaDTOS.getTamanho());
        assertEquals(tamanhoEsperado, entrevistaDTOS.getElementos().size());
    }

    @Test
    public void deveRetornarUmaListaPorMes() {
        final int tamanhoEsperado = 1;
        final int pagina = 0;
        final int tamanho = 5;

        EntrevistaEntity entrevista = getEntrevistaEntity();
        PageImpl<EntrevistaEntity> page =
                new PageImpl<>(List.of(entrevista), PageRequest.of(pagina, tamanho), 0);

        when(entrevistaRepository.findAllByMes(anyInt(), anyInt(), any())).thenReturn(page);

        PageDTO<EntrevistaDTO> entrevistaDTOS = entrevistaService.listMes(pagina, tamanho, 11, 2022);

        assertEquals(pagina, entrevistaDTOS.getPagina());
        assertEquals(tamanho, entrevistaDTOS.getTamanho());
        assertEquals(tamanhoEsperado, entrevistaDTOS.getElementos().size());
    }

    @Test
    public void deveConverterCorretamenteEntrevistaEntityParaEntrevistaDTO() {
        final int idEsperado = 1;
        final String nomeCandidatoEsperado = "Heloise Isabela Lopes";
        final String nomeUsuarioesperado = "Débora Sophia da Silva";

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        CandidatoEntity candidato = getCandidatoEntity();

        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setUsuarioEntity(usuarioEntity);
        entrevistaEntity.setCandidatoEntity(candidato);

        UsuarioDTO usuarioDTO = getUsuarioDTO();
        CandidatoDTO candidatoDTO = getCandidatoDTO();

        when(usuarioService.converterEmDTO(any())).thenReturn(usuarioDTO);
        when(candidatoService.converterEmDTO(any())).thenReturn(candidatoDTO);

        EntrevistaDTO entrevistaDTO = entrevistaService.converterParaEntrevistaDTO(entrevistaEntity);

        assertEquals(idEsperado, entrevistaDTO.getIdEntrevista());
        assertEquals(nomeCandidatoEsperado, entrevistaDTO.getCandidatoDTO().getNomeCompleto());
        assertEquals(nomeUsuarioesperado, entrevistaDTO.getUsuarioDTO().getNomeCompleto());
    }

    @Test
    public void deveCadastrarUmaEntrevistaCorretamente() throws RegraDeNegocioException {
        final int idEsperado = 1;
        final String token = "token";

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();

        CandidatoEntity candidato = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setCandidatoEntity(candidato);

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());
        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of());
        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);
        when(tokenService.getTokenConfirmacao(any())).thenReturn(token);

        EntrevistaDTO entrevistaDTO = entrevistaService.createEntrevista(entrevistaCreateDTO);

        assertEquals(idEsperado, entrevistaDTO.getIdEntrevista());
        assertEquals("Santana", entrevistaDTO.getCidade());
        assertEquals("AP", entrevistaDTO.getEstado());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.empty());

        entrevistaService.createEntrevista(entrevistaCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioJaEstiverOcupado() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setUsuarioEntity(usuarioEntity);

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());
        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaEntity));

        entrevistaService.createEntrevista(entrevistaCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoCandidaoNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        entrevistaService.createEntrevista(entrevistaCreateDTO);
    }

    @Test
    public void deveBuscarEntrevistaPeloCandidatoCorretamente() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setCandidatoEntity(candidatoEntity);

        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        EntrevistaEntity entrevistaRetornada = entrevistaService.findByCandidatoEntity(candidatoEntity);

        assertEquals(1, entrevistaRetornada.getIdEntrevista());
        assertEquals(candidatoEntity.getNomeCompleto(), entrevistaEntity.getCandidatoEntity().getNomeCompleto());
        assertEquals(candidatoEntity.getEmail(), entrevistaEntity.getCandidatoEntity().getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoCandidatoNaoTiverEntrevistas() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();

        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());

        entrevistaService.findByCandidatoEntity(candidatoEntity);
    }

    @Test
    public void deveDeletarUmaEntrevistaCorretamente() throws RegraDeNegocioException {
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));

        entrevistaService.deletarEntrevista(1);
        verify(entrevistaRepository).delete(any());
    }

    @Test
    public void deveAtualizarEntrevistaCorretamente() throws RegraDeNegocioException {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of());
        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);

        EntrevistaDTO entrevistaDTO =
                entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CONFIRMADA);

        assertEquals(1, entrevistaDTO.getIdEntrevista());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoForCadastrado() throws RegraDeNegocioException {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.empty());
        entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CANCELADA);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoListaDeEntrevistasVazia() throws RegraDeNegocioException {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setUsuarioEntity(usuarioEntity);

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaEntity));

        entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CANCELADA);
    }

    private static EntrevistaEntity getEntrevistaEntity() {
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setIdEntrevista(1);
        entrevistaEntity.setDataEntrevista(LocalDateTime.now().plusDays(1));
        entrevistaEntity.setCidade("Santana");
        entrevistaEntity.setEstado("AP");
        entrevistaEntity.setObservacoes("Sem observações.");
        entrevistaEntity.setLegenda(Legenda.PENDENTE);

        return entrevistaEntity;
    }

    private static CandidatoEntity getCandidatoEntity() {
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNotaProva(8.00);
        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setGenero(Genero.FEMININO);
        candidatoEntity.setAtivo('T');

        return candidatoEntity;
    }

    private static CandidatoDTO getCandidatoDTO() {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setNomeCompleto("Heloise Isabela Lopes");
        candidatoDTO.setCidade("Santana");
        candidatoDTO.setEstado("AP");
        candidatoDTO.setEmail("heloise.lopes@dbccompany.com.br");
//        candidatoDTO.setGenero(Genero.FEMININO);
        candidatoDTO.setAtivo('T');

        return candidatoDTO;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNomeCompleto("Débora Sophia da Silva");
        usuarioEntity.setEmail("debora.silva@dbccompany.com.br");
        usuarioEntity.setGenero(Genero.FEMININO);
        usuarioEntity.setCidade("Mossoró");
        usuarioEntity.setEstado("RN");
        usuarioEntity.setAtivo('T');

        return usuarioEntity;
    }

    private static UsuarioDTO getUsuarioDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1);
        usuarioDTO.setNomeCompleto("Débora Sophia da Silva");
        usuarioDTO.setEmail("debora.silva@dbccompany.com.br");
        usuarioDTO.setGenero(Genero.FEMININO);
        usuarioDTO.setCidade("Mossoró");
        usuarioDTO.setEstado("RN");
        usuarioDTO.setAtivo('T');

        return usuarioDTO;
    }
}
