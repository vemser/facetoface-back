package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.edicao.EdicaoDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EdicaoRepository;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static br.com.vemser.facetoface.factory.EdicaoFactory.getEdicaoEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EdicaoServiceTest {

    @InjectMocks
    private EdicaoService edicaoService;

    @Mock
    private EdicaoRepository edicaoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(edicaoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornarEdicaoQuandoEdicaoEstiverCadastradaNoBancoPeloIDComSucesso() throws RegraDeNegocioException {
        // SETUP
        final int ID_EDICAO_ESPERADO = 2;
        final String nome = "10";
        EdicaoEntity edicaoEsperada = new EdicaoEntity();
        edicaoEsperada.setIdEdicao(ID_EDICAO_ESPERADO);
        edicaoEsperada.setNome(nome);

        // ACT
        when(edicaoRepository.findById(ID_EDICAO_ESPERADO)).thenReturn(Optional.of(edicaoEsperada));
        EdicaoEntity edicaoResponse = edicaoService.findById(ID_EDICAO_ESPERADO);

        // ASSERT
        assertEquals(ID_EDICAO_ESPERADO, edicaoResponse.getIdEdicao());
        assertEquals(nome, edicaoEsperada.getNome());
    }

    @Test
    public void deveCriarUmaNovaEdicaoCasoNaoExistaONomeNoBanco() {
        final String edicaoNome = "Edição 10";
        EdicaoEntity edicao = getEdicaoEntity();

        when(edicaoRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(edicaoRepository.save(any())).thenReturn(edicao);

        EdicaoEntity edicaoRetornada = edicaoService.findByNome(edicaoNome);

        assertEquals(edicaoNome, edicaoRetornada.getNome());
    }

    @Test
    public void deveRetornarEdicaoQuandoEdicaoEstiverCadastradaNoBancoPeloNomeComSucesso() throws RegraDeNegocioException {
        //Setup
        String nome = "Edição 10";

        final int ID_EDICAO_ESPERADO = 2;
        EdicaoEntity edicaoEsperada = new EdicaoEntity();
        edicaoEsperada.setNome(nome);
        edicaoEsperada.setIdEdicao(ID_EDICAO_ESPERADO);
        // Act
        when(edicaoRepository.findByNome(anyString())).thenReturn(Optional.of(edicaoEsperada));
        EdicaoEntity edicaoResponse = edicaoService.findByNome(nome);

        // Assert
        assertEquals(nome, edicaoResponse.getNome());
    }

    @Test
    public void deveTestarConverterEdicaoDTOParaEntityComSucesso() {
        //Setup
        EdicaoDTO edicao = getEdicaoDTO();

        //Act
        EdicaoEntity edicaoEntityResponse = edicaoService.converterEntity(edicao);
        //Assert
        assertEquals(edicaoEntityResponse.getNome(), edicao.getNome());
    }

    @Test
    public void deveTestarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        //Setup
        final int idEdicao = 1;
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        // Act
        when(edicaoRepository.findById(idEdicao)).thenReturn(Optional.of(edicaoEntity));
        edicaoService.deleteFisico(edicaoEntity.getIdEdicao());

        //Assert
        verify(edicaoRepository).deleteById(idEdicao);

    }

    @Test
    public void testarCriarEdicaoCasoNaoExistaUmaComSucesso() {
        //Setup
        final String nome = "12";
        EdicaoDTO edicaoDTO = new EdicaoDTO();
        edicaoDTO.setNome(nome);
        final int id = 2;
        EdicaoEntity edicaoEntity = new EdicaoEntity();
        edicaoEntity.setNome(nome);
        edicaoEntity.setIdEdicao(id);
        //Act
        when(edicaoRepository.save(any())).thenReturn(edicaoEntity);
        EdicaoEntity edicaoEntityResponse = edicaoService.create(edicaoDTO);
        //Assert
        assertEquals(2, edicaoEntityResponse.getIdEdicao());
        assertEquals(edicaoEntity.getNome(), edicaoEntityResponse.getNome());
        assertEquals(edicaoEntity.getIdEdicao(), edicaoEntityResponse.getIdEdicao());
    }

    @Test
    public void deveTestarCriacaoComSucesso() {
        // Setup
        EdicaoDTO edicaoDTO = getEdicaoDTO();
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        Set<CandidatoEntity> lista = new HashSet<>();
        lista.add(candidatoEntity);
        edicaoEntity.setCandidatoEntities(lista);
        // Act
        when(edicaoRepository.save(any())).thenReturn(edicaoEntity);
        EdicaoEntity edicaoDTOResponse = edicaoService.create(edicaoDTO);
        // Assert
        assertEquals(1, edicaoDTOResponse.getIdEdicao());
        assertEquals(edicaoEntity.getNome(), edicaoDTOResponse.getNome());
        assertEquals(edicaoEntity.getIdEdicao(), edicaoDTOResponse.getIdEdicao());
        assertEquals(1, edicaoDTOResponse.getCandidatoEntities().size());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarBuscaEdicaoPorIdComErro() throws RegraDeNegocioException {
        final int id = 1;
        when(edicaoRepository.findById(id)).thenReturn(Optional.empty());
        edicaoService.findById(id);
    }

    private static EdicaoDTO getEdicaoDTO() {
        final String nome = "10";
        EdicaoDTO edicaoDTO = new EdicaoDTO();
        edicaoDTO.setNome(nome);
        return edicaoDTO;
    }
}
