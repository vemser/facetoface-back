package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.*;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import br.com.vemser.facetoface.repository.ImageRepository;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.HexFormat;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test(expected = RegraDeNegocioException.class)
    public void devePegarImagemUsuarioComErro() throws RegraDeNegocioException {
        //Setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        //Act
        when(imageRepository.findByCandidato(any())).thenReturn(Optional.empty());
        String imagemBase64 = imageService.pegarImagemCandidato(usuarioEntity.getEmail());
    }

    @Test
    public void devePegarImagemUsuarioComSucesso() throws RegraDeNegocioException {
        //Setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        //Act
        when(usuarioService.findByEmail(any())).thenReturn(Optional.of(usuarioEntity));
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

//    @Test
//    public void deveTestarDeleteFisicoComSucesso() throws RegraDeNegocioException {
//        //Setup
//        ImageEntity imageEntity = getImageEntity();
//        Integer id = 1;
//        //Act
//        when(imageRepository.findById(id)).thenReturn(Optional.of(imageEntity));
//        imageService.deleteFisico(1);
//        //Assert
//        verify(imageRepository).save(any());
//    }




    private static ImageEntity getImageEntity() {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setCandidato(getCandidatoEntity());
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        byte[] bytes = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        imageEntity.setData(bytes);
        imageEntity.setIdImagem(1);
        imageEntity.setTipo("png");
        return imageEntity;
    }

    private static CandidatoEntity getCandidatoEntity() {
        LinguagemEntity linguagemEntity = getLinguagemEntity();
        Set<LinguagemEntity> linguagemList = new HashSet<>();
        linguagemList.add(linguagemEntity);

        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNotaProva(8.00);
        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setGenero(Genero.FEMININO);
        candidatoEntity.setLinguagens(linguagemList);
        candidatoEntity.setEdicao(getEdicaoEntity());
        candidatoEntity.setTrilha(getTrilhaEntity());
        candidatoEntity.setAtivo('T');

        return candidatoEntity;
    }

    private static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setSenha("123");
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setGenero(Genero.MASCULINO);
        usuarioEntity.setTrilha(getTrilhaEntity());
        usuarioEntity.setAtivo('T');
        usuarioEntity.setNomeCompleto("Heloise Isabela Lopes");
        usuarioEntity.setCidade("Santana");
        usuarioEntity.setEstado("AP");
        usuarioEntity.setEmail("heloise.lopes@dbccompany.com.br");
        return usuarioEntity;
    }
    private static LinguagemEntity getLinguagemEntity() {
        LinguagemEntity linguagemEntity = new LinguagemEntity();
        linguagemEntity.setIdLinguagem(1);
        linguagemEntity.setNome("Java");

        return linguagemEntity;
    }

    private static TrilhaEntity getTrilhaEntity() {
        TrilhaEntity trilha = new TrilhaEntity();
        trilha.setIdTrilha(1);
        trilha.setNome("BACKEND");

        return trilha;
    }

    private static EdicaoEntity getEdicaoEntity() {
        EdicaoEntity edicao = new EdicaoEntity();
        edicao.setIdEdicao(1);
        edicao.setNome("Edição 10");

        return edicao;
    }
}
