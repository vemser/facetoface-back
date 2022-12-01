package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.linguagem.LinguagemDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.LinguagemRepository;
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

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoEntity;
import static br.com.vemser.facetoface.factory.LinguagemFactory.getLinguagemDTO;
import static br.com.vemser.facetoface.factory.LinguagemFactory.getLinguagemEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinguagemServiceTest {

    @InjectMocks
    private LinguagemService linguagemService;

    @Mock
    private LinguagemRepository linguagemRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(linguagemService, "objectMapper", objectMapper);
    }

    @Test
    public void testarBuscarLinguagemPeloNomeComSucesso() {
        //Setup
        final String NOME = "Java";
        final int id = 1;
        LinguagemEntity linguagemEntity = new LinguagemEntity();
        linguagemEntity.setNome(NOME);
        linguagemEntity.setIdLinguagem(id);

        //Act
        when(linguagemRepository.findByNome(anyString())).thenReturn(Optional.of(linguagemEntity));
        LinguagemEntity linguagemResponse = linguagemService.findByNome(NOME);

        //Assert
        assertEquals(NOME, linguagemResponse.getNome());
    }

    @Test
    public void deveCriarUmaNovaLinguagemCasoNaoExistaONomeNoBanco() {
        final String linguagemNome = "Java";
        LinguagemEntity linguagem = getLinguagemEntity();

        when(linguagemRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(linguagemRepository.save(any())).thenReturn(linguagem);

        LinguagemEntity linguagemRetornada = linguagemService.findByNome(linguagemNome);

        assertEquals(linguagemNome, linguagemRetornada.getNome());
    }

    @Test
    public void tentarConverterLinguagemDTOParaEntityComSucesso() {
        //Setup
        LinguagemDTO linguagemDTO = getLinguagemDTO();

        // Act

        LinguagemEntity linguagemResponse = linguagemService.converterEntity(linguagemDTO);
        //Assert
        assertEquals(linguagemResponse.getNome(), linguagemDTO.getNome());
    }

    @Test
    public void deveTestarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        //Setup
        final int idLinguagem = 1;
        LinguagemEntity linguagem = getLinguagemEntity();

        // Act
        when(linguagemRepository.findById(idLinguagem)).thenReturn(Optional.of(linguagem));
        linguagemService.deleteFisico(linguagem.getIdLinguagem());

        //Assert
        verify(linguagemRepository).deleteById(idLinguagem);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarBuscaLinguagemPorIdComErro() throws RegraDeNegocioException {
        final int id = 1;
        when(linguagemRepository.findById(id)).thenReturn(Optional.empty());
        linguagemService.findById(id);
    }

    @Test
    public void deveTestarCriacaoComSucesso() {
        // Setup
        LinguagemDTO linguagemDTO = getLinguagemDTO();
        LinguagemEntity linguagem = getLinguagemEntity();
        Set<CandidatoEntity> lista = new HashSet<>();
        lista.add(getCandidatoEntity());
        linguagem.setCandidatoEntities(lista);
        // Act
        when(linguagemRepository.save(any())).thenReturn(linguagem);
        LinguagemEntity linguagemDTOResponse = linguagemService.create(linguagemDTO);
        // Assert
        assertEquals(1, linguagemDTOResponse.getIdLinguagem());
        assertEquals(linguagem.getNome(), linguagemDTOResponse.getNome());
        assertEquals(linguagem.getIdLinguagem(), linguagemDTOResponse.getIdLinguagem());
        assertEquals(1, linguagemDTOResponse.getCandidatoEntities().size());
    }

    @Test
    public void testarCriarLinguagemCasoNaoExistaUmaComSucesso() {
        //Setup
        final String nome = "Python";
        LinguagemDTO linguagemDTO = new LinguagemDTO();
        linguagemDTO.setNome(nome);
        final int id = 2;
        LinguagemEntity linguagem = new LinguagemEntity();
        linguagem.setNome(nome);
        linguagem.setIdLinguagem(id);
        //Act
        when(linguagemRepository.save(any())).thenReturn(linguagem);
        LinguagemEntity linguagemEntityResponse = linguagemService.create(linguagemDTO);
        //Assert
        assertEquals(2, linguagemEntityResponse.getIdLinguagem());
        assertEquals(linguagem.getNome(), linguagemEntityResponse.getNome());
        assertEquals(linguagem.getIdLinguagem(), linguagemEntityResponse.getIdLinguagem());
    }
}
