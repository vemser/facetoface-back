package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private ObjectMapper objectMapper = new ObjectMapper();

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
