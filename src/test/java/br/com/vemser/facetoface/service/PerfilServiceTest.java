package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.perfil.PerfilDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.PerfilRepository;
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

import java.util.Collections;
import java.util.Optional;

import static br.com.vemser.facetoface.factory.PerfilFactory.getPerfilEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PerfilServiceTest {
    @InjectMocks
    private PerfilService perfilService;

    @Mock
    private PerfilRepository perfilRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(perfilService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        Integer id = 10;

        when(perfilRepository.findById(anyInt())).thenReturn(Optional.of(getPerfilEntity()));
        perfilService.deleteFisico(id);

        verify(perfilRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeleteComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(perfilRepository.findById(anyInt())).thenReturn(Optional.empty());
        perfilService.deleteFisico(id);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 2;

        when(perfilRepository.findById(anyInt())).thenReturn(Optional.of(getPerfilEntity()));
        PerfilEntity perfil = perfilService.findById(id);

        assertNotNull(perfil);
        assertEquals(perfil.getIdPerfil(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(perfilRepository.findById(anyInt())).thenReturn(Optional.empty());
        perfilService.findById(id);
    }

    @Test
    public void deveTestarFindByNomeComSucesso() throws RegraDeNegocioException {
        String nome = "ADMIN";

        when(perfilRepository.findByNome(anyString())).thenReturn(Optional.of(getPerfilEntity()));
        PerfilEntity perfil = perfilService.findByNome(nome);

        assertNotNull(perfil);
        assertEquals(perfil.getNome(), nome);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByNomeComErro() throws RegraDeNegocioException {
        String nome = "ADMIN";
        when(perfilRepository.findByNome(anyString())).thenReturn(Optional.empty());
        perfilService.findByNome(nome);
    }

    @Test
    public void deveConverterEmDTOComSucesso(){
        PerfilEntity perfilEntity = getPerfilEntity();
        PerfilDTO perfilDTO = perfilService.convertToDTO(perfilEntity);

        assertNotNull(perfilDTO);
        assertEquals(perfilEntity.getNome(), perfilDTO.getNome());
    }
}
