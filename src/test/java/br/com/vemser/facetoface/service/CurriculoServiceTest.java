package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.factory.CurriculoFactory;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HexFormat;
import java.util.Optional;

import static br.com.vemser.facetoface.factory.CandidatoFactory.getCandidatoEntity;
import static br.com.vemser.facetoface.factory.CurriculoFactory.getCurriculoEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CurriculoServiceTest {
    @InjectMocks
    private CurriculoService curriculoService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private CurriculoRepository curriculoRepository;


    @Test
    public void deveTestarArquivarCurriculoComSucessoCandidatoSemCurriculo() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo.pdf", bytes);


        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.empty());
        when(curriculoRepository.save(any())).thenReturn(getCurriculoEntity());

        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());


        verify(curriculoRepository, times(1)).save(any());
    }
    @Test
    public void deveTestarArquivarCurriculoComSucesso() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo", bytes);


        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.of(getCurriculoEntity()));
        when(curriculoRepository.save(any())).thenReturn(getCurriculoEntity());

        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());


        verify(curriculoRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarUploadCurriculoSemSerPDFComErro() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo", bytes);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.of(getCurriculoEntity()));
        when(curriculoRepository.save(any())).thenReturn(getCurriculoEntity());

        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());

    }

    @Test(expected = IOException.class)
    public void deveTestarArquivarCurriculoComRegrasDeNegocioException() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        final MultipartFile curriculo = Mockito.mock(MultipartFile.class, Mockito.RETURNS_DEEP_STUBS);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.of(getCurriculoEntity()));
        when(curriculo.getBytes()).thenThrow(new IOException("Teste"));

        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());
    }
    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarArquivarCurriculoComIOException() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile curriculo = new MockMultipartFile("curriculo", bytes);


        when(candidatoService.findByEmailEntity(any())).thenThrow(new RegraDeNegocioException("Erro"));
        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());
    }

    @Test
    public void devePegarCurriculoCandidatoComSucesso() throws RegraDeNegocioException{
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.of(getCurriculoEntity()));
        String curriculoBase64 = curriculoService.pegarCurriculoCandidato(candidatoEntity.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarCurriculoCandidatoComErro() throws RegraDeNegocioException {
        //Setup
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");
        //Act
        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidatoEntity);
        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.empty());
        String imagemBase64 = curriculoService.pegarCurriculoCandidato(candidatoEntity.getEmail());
        //Assert
        assertNotNull(imagemBase64);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        Integer id = 2;

        when(curriculoRepository.findById(anyInt())).thenReturn(Optional.of(getCurriculoEntity()));
        CurriculoEntity curriculo = curriculoService.findById(id);

        assertNotNull(curriculo);
        assertEquals(curriculo.getIdCurriculo(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void findByIdComErro() throws RegraDeNegocioException {
        Integer id = 2;
        when(curriculoRepository.findById(anyInt())).thenReturn(Optional.empty());
        curriculoService.findById(id);
    }

    @Test
    public void testarDeletarFisicamenteUsuarioComSucesso() throws RegraDeNegocioException {
        CurriculoEntity curriculoEntity = CurriculoFactory.getCurriculoEntity();

        when(curriculoRepository.findById(any())).thenReturn(Optional.of(curriculoEntity));
        curriculoService.deleteFisico(1);

        verify(curriculoRepository).deleteById(any());
    }
    @Test(expected = RegraDeNegocioException.class)
    public void testarDeletarFisicamenteUsuarioComErro() throws RegraDeNegocioException {

        when(curriculoRepository.findById(any())).thenReturn(Optional.empty());
        curriculoService.deleteFisico(1);

        verify(curriculoRepository).deleteById(any());
    }
}
