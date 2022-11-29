package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.TrilhaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrilhaServiceTest {
    @InjectMocks
    private TrilhaService trilhaService;

    @Mock
    private TrilhaRepository trilhaRepository;

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        Integer id = 10;

        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.of(getTrilhaEntity()));
        trilhaService.deleteFisico(id);

        verify(trilhaRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeleteComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.empty());
        trilhaService.deleteFisico(id);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 2;

        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.of(getTrilhaEntity()));
        TrilhaEntity trilhaEntity = trilhaService.findById(id);

        assertNotNull(trilhaEntity);
        assertEquals(trilhaEntity.getIdTrilha(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(trilhaRepository.findById(anyInt())).thenReturn(Optional.empty());
        trilhaService.findById(id);
    }

    @Test
    public void deveTestarFindByNomeComSucesso() throws RegraDeNegocioException {
        String nome = "QA";

        when(trilhaRepository.findByNome(anyString())).thenReturn(Optional.of(getTrilhaEntity()));
        TrilhaEntity trilhaEntity = trilhaService.findByNome(nome);

        assertNotNull(trilhaEntity);
        assertEquals(trilhaEntity.getNome(), nome);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByNomeComErro() throws RegraDeNegocioException {
        String nome = "QA";
        when(trilhaRepository.findByNome(anyString())).thenReturn(Optional.empty());
        trilhaService.findByNome(nome);
    }


    private static TrilhaEntity getTrilhaEntity() {
        return new TrilhaEntity(2,
                "QA",
                Collections.emptySet(),
                Collections.emptySet()
        );
    }

    private static TrilhaDTO getTrilhaDTO() {
        return new TrilhaDTO("QA");
    }
}

