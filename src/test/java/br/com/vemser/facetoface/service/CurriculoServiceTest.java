package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HexFormat;
import java.util.Optional;

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

    @Mock
    private CandidatoRepository candidatoRepository;


//    @Test
//    public void deveTestarArquivarCurriculoComSucesso() throws RegraDeNegocioException, IOException {
//        CandidatoEntity candidatoEntity = new CandidatoEntity();
//        candidatoEntity.setIdCandidato(2);
//        candidatoEntity.setEmail("teste@gmail.com.br");
//        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
//        MultipartFile curriculo = new MockMultipartFile("imagem", bytes);
//        String nomeArquivo = StringUtils.cleanPath(curriculo.getOriginalFilename());
//
//
//        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
//        when(curriculoRepository.findByCandidato(any())).thenReturn(Optional.of(getCurriculoEntity()));
//        when(curriculoRepository.save(any())).thenReturn(getCurriculoEntity());
//
//        curriculoService.arquivarCurriculo(curriculo, candidatoEntity.getEmail());
//
//
//        verify(curriculoRepository, times(1)).save(getCurriculoEntity());
//
//    }

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


    private static CurriculoEntity getCurriculoEntity(){
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        return new CurriculoEntity(2,
                "curriculo",
                "png",
                bytes,
                candidatoEntity);
    }
}
