package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.factory.ImageFactory;
import br.com.vemser.facetoface.repository.ImageRepository;
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
import static br.com.vemser.facetoface.factory.ImageFactory.getImageEntity;
import static br.com.vemser.facetoface.factory.UsuarioFactory.getUsuarioEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UsuarioService usuarioService;

    @Test
    public void deveTestarUploadCandidatoImagemComSucesso() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void testarUploadImagemCandidatoCasoExistaComSucesso() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarUploadUsuarioCasoExistaImagemComSucesso() throws RegraDeNegocioException, IOException {
        UsuarioEntity usuario = getUsuarioEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(usuarioService.findByEmail(any())).thenReturn(usuario);
        when(imageRepository.findByUsuario(any())).thenReturn(Optional.of(getImageEntity()));
        when(imageRepository.save(any())).thenReturn(ImageFactory.getImageUsuario());

        imageService.arquivarUsuario(imagem, usuario.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void testarUploadImagemUsuarioCasoNaoExistaComSucesso() throws RegraDeNegocioException, IOException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);
        when(imageRepository.findByUsuario(any())).thenReturn(Optional.empty());
        when(imageRepository.save(any())).thenReturn(getImageEntity());

        imageService.arquivarUsuario(imagem, usuarioEntity.getEmail());

        verify(imageRepository, times(1)).save(any());
    }

    @Test
    public void devePegarImagemCandidatoComSucesso() throws RegraDeNegocioException {
        //Setup
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(2);
        candidatoEntity.setEmail("teste@gmail.com.br");
        //Act
        when(candidatoService.findByEmailEntity(any())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));
        String imagemBase64 = imageService.pegarImagemCandidato(candidatoEntity.getEmail());
        //Assert
        assertNotNull(imagemBase64);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemCandidatoComErro() throws RegraDeNegocioException {
        //Setup
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        //Act
        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidatoEntity);
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.empty());
        String imagemBase64 = imageService.pegarImagemCandidato(candidatoEntity.getEmail());
    }

    @Test(expected = IOException.class)
    public void deveTestarArquivarImagemCandidatoComIOException() throws RegraDeNegocioException, IOException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        final MultipartFile imagem = Mockito.mock(MultipartFile.class, Mockito.RETURNS_DEEP_STUBS);
        ImageEntity imageEntity = getImageEntity();
        imageEntity.setCandidato(candidatoEntity);
        imageEntity.setData(imagem.getBytes());

        when(candidatoService.findByEmailEntity(any())).thenReturn(getCandidatoEntity());
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.of(getImageEntity()));
        when(imagem.getBytes()).thenThrow(new IOException("Teste"));

        imageService.arquivarCandidato(imagem, candidatoEntity.getEmail());
    }

    @Test(expected = IOException.class)
    public void deveTestarArquivarImagemUsuarioComIOException() throws RegraDeNegocioException, IOException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setImageEntity(getImageEntity());
        final MultipartFile imagem = Mockito.mock(MultipartFile.class, Mockito.RETURNS_DEEP_STUBS);

        when(usuarioService.findByEmail(any())).thenReturn(getUsuarioEntity());
        when(imageRepository.findByUsuario(any())).thenReturn(Optional.of(getImageEntity()));
        when(imagem.getBytes()).thenThrow(new IOException("Teste"));

        imageService.arquivarUsuario(imagem, usuarioEntity.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemCandidatoComEmailComErro() throws RegraDeNegocioException {
        //Setup
        final String emailEsperado = "abc@dbccompany.com.br";
        //Act
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.empty());
        imageService.pegarImagemCandidato(emailEsperado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemUsuarioComEmailComErro() throws RegraDeNegocioException {
        //Setup
        final String emailEsperado = "abc@dbccompany.com.br";
        //Act
        imageService.pegarImagemUsuario(emailEsperado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemUsuarioImagemComErro() throws RegraDeNegocioException {
        //Setup
        final Integer id = 2;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Assert
        imageService.findById(2);
    }

//    @Test(expected = RegraDeNegocioException.class)
//    public void deveArquivarUsuarioComEmailDarErro() throws RegraDeNegocioException {
//        //Setup
//        final String email = "abc@dbccompany.com.br";
//        // act
//        when(usuarioService.findByEmail(any())).thenReturn(Optional.empty());
//
//    }

    @Test
    public void devePegarImagemUsuarioComSucesso() throws RegraDeNegocioException {
        //Setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        //Act
        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);
        when(imageRepository.findByUsuario(any())).thenReturn(Optional.of(getImageEntity()));
        String imagemBase64 = imageService.pegarImagemUsuario(usuarioEntity.getEmail());
        //Assert
        assertNotNull(imagemBase64);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws RegraDeNegocioException {
        //Setup
        Integer id = 1;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.of(getImageEntity()));
        ImageEntity imageEntity = imageService.findById(id);
        //Assert
        assertNotNull(imageEntity);
        assertEquals(imageEntity.getIdImagem(), id);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindByIdComErro() throws RegraDeNegocioException {
        //Setup
        Integer id = 2;
        //Act
        when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Assert
        imageService.findById(id);
    }

    @Test
    public void testarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        final Integer id = 1;
        when(imageRepository.findById(anyInt())).thenReturn(Optional.of(getImageEntity()));
        imageService.deleteFisico(id);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarDeleteFisicoComErro() throws RegraDeNegocioException {
        final Integer id = 2;

        imageService.deleteFisico(id);
    }

//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoUsuarioNaoForEncontrado() throws RegraDeNegocioException, IOException {
//        UsuarioEntity usuario = UsuarioFactory.getUsuarioEntity();
//        byte[] imagemBytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
//        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
//
//        when(usuarioService.findOptionalByEmail(any())).thenReturn(Optional.empty());
//
//        imageService.arquivarUsuario(imagem, usuario.getEmail());
//    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoTiverFotoCadastrada() throws RegraDeNegocioException {
        final String email = "Heloise Isabela Lopes";

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);

        imageService.pegarImagemUsuario(email);
    }
}
