package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.*;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.factory.EdicaoFactory;
import br.com.vemser.facetoface.factory.PerfilFactory;
import br.com.vemser.facetoface.factory.TrilhaFactory;
import br.com.vemser.facetoface.factory.UsuarioFactory;
import br.com.vemser.facetoface.repository.UsuarioRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.constraints.Email;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilService perfilService;
    @Mock
    private EdicaoService edicaoService;
    @Mock
    private TrilhaService trilhaService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void testarCadastarUsuarioComSucesso() throws RegraDeNegocioException{
        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
        final String trilhaEsperado = "BACKEND";
        final String senhaCriptografada = "j183nsur74bd83gr7";

        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
        PerfilEntity perfil = PerfilFactory.getPerfilEntity();

        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
        when(perfilService.findByNome(anyString())).thenReturn(perfil);
        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);

        UsuarioDTO usuarioDTO = usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);

        assertEquals(usuarioDTO.getNomeCompleto(), usuarioEntity.getNomeCompleto());
        assertEquals(trilhaEsperado, usuarioDTO.getTrilha().getNome());
        assertEquals(emailEsperado, usuarioDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarCadastarUsuarioComErro() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));

        usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);
    }

    @Test
    public void testarAtualizarUsuarioComSucesso() throws RegraDeNegocioException{
        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
        PerfilEntity perfil = PerfilFactory.getPerfilEntity();

        UsuarioEntity usuarioSalvo = UsuarioFactory.getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
        when(perfilService.findByNome(anyString())).thenReturn(perfil);
        when(usuarioRepository.save(any())).thenReturn(usuarioSalvo);

        UsuarioDTO usuarioDTO = usuarioService.update(1, usuarioCreateDTO, Genero.FEMININO);

        assertEquals(usuarioSalvo.getEmail(), usuarioDTO.getEmail());
        assertEquals(usuarioSalvo.getPerfis().size(), usuarioDTO.getPerfis().size());
    }
}
