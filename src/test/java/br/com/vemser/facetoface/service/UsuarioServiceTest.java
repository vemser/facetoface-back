//package br.com.vemser.facetoface.service;
//
//import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
//import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
//import br.com.vemser.facetoface.dto.login.LoginRetornoDTO;
//import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
//import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
//import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
//import br.com.vemser.facetoface.entity.*;
//import br.com.vemser.facetoface.entity.enums.Genero;
//import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
//import br.com.vemser.facetoface.factory.EdicaoFactory;
//import br.com.vemser.facetoface.factory.PerfilFactory;
//import br.com.vemser.facetoface.factory.TrilhaFactory;
//import br.com.vemser.facetoface.factory.UsuarioFactory;
//import br.com.vemser.facetoface.repository.UsuarioRepository;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import javax.validation.constraints.Email;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class UsuarioServiceTest {
//
//    @InjectMocks
//    private UsuarioService usuarioService;
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @Mock
//    private PerfilService perfilService;
//    @Mock
//    private EdicaoService edicaoService;
//    @Mock
//    private TrilhaService trilhaService;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
//    }
//
//    @Test
//    public void testarCadastarUsuarioComSucesso() throws RegraDeNegocioException{
//        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
//        final String trilhaEsperado = "BACKEND";
//        final String senhaCriptografada = "j183nsur74bd83gr7";
//
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
//        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
//        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
//        PerfilEntity perfil = PerfilFactory.getPerfilEntity();
//
//        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
//        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
//        when(perfilService.findByNome(anyString())).thenReturn(perfil);
//        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
//        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);
//
//        UsuarioDTO usuarioDTO = usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);
//
//        assertEquals(usuarioDTO.getNomeCompleto(), usuarioEntity.getNomeCompleto());
//        assertEquals(trilhaEsperado, usuarioDTO.getTrilha().getNome());
//        assertEquals(emailEsperado, usuarioDTO.getEmail());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void testarCadastarUsuarioComErro() throws RegraDeNegocioException {
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
//
//        usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);
//    }
//
//    @Test
//    public void testarAtualizarUsuarioComSucesso() throws RegraDeNegocioException{
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
//        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
//        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
//        PerfilEntity perfil = PerfilFactory.getPerfilEntity();
//
//        UsuarioEntity usuarioSalvo = UsuarioFactory.getUsuarioEntity();
//
//        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
//        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
//        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
//        when(perfilService.findByNome(anyString())).thenReturn(perfil);
//        when(usuarioRepository.save(any())).thenReturn(usuarioSalvo);
//
//        UsuarioDTO usuarioDTO = usuarioService.update(1, usuarioCreateDTO, Genero.FEMININO);
//
//        assertEquals(usuarioSalvo.getEmail(), usuarioDTO.getEmail());
//        assertEquals(usuarioSalvo.getPerfis().size(), usuarioDTO.getPerfis().size());
//    }
//
//    @Test
//    public void testarBuscarUsuarioDTOPorEmailComSucesso() throws RegraDeNegocioException{
//        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
//        UsuarioDTO usuarioDTO = usuarioService.findByEmailDTO(emailEsperado);
//
//        assertEquals(emailEsperado, usuarioDTO.getEmail());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void testarBuscarUsuarioDTOPorEmailComSErro() throws RegraDeNegocioException{
//        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn((Optional.empty()));
//        usuarioService.findByEmailDTO(emailEsperado);
//    }
//
//    @Test
//    public void testarRetornoListaPaginadaComSucesso() {
//        final int pagina = 0;
//        final int tamanho = 5;
//
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        PageImpl<UsuarioEntity> usuarioEntities =
//                new PageImpl<>(List.of(usuarioEntity), PageRequest.of(pagina, tamanho), 0);
//
//        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(usuarioEntities);
//
//        PageDTO<UsuarioDTO> usuarioDTOPaginado = usuarioService.list(pagina, tamanho);
//
//        assertEquals(pagina, usuarioDTOPaginado.getPagina());
//        assertEquals(1, usuarioDTOPaginado.getElementos().size());
//        assertEquals(tamanho, usuarioDTOPaginado.getTamanho());
//    }
//
//    @Test
//    public void testarDeletarLogicamenteUsuarioComSucesso() throws RegraDeNegocioException{
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//
//        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
//        usuarioService.deleteLogico(1);
//
//        verify(usuarioRepository).save(any());
//    }
//
//    @Test
//    public void testarDeletarFisicamenteUsuarioComSucesso() throws RegraDeNegocioException{
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//
//        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
//        usuarioService.deleteFisico(1);
//
//        verify(usuarioRepository).deleteById(any());
//    }
//
//    @Test
//    public void testarBuscarEmailUsuarioComSucesso() throws RegraDeNegocioException{
//        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
//        Optional<UsuarioEntity> usuario = usuarioService.findByEmail(emailEsperado);
//
//        assertEquals(usuario.get().getEmail(), emailEsperado);
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void testarBuscarEmailUsuarioComErro() throws RegraDeNegocioException{
//        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        usuarioService.findByEmail(emailEsperado);
//    }
//
//    @Test
//    public void testarBuscarLoginComsucesso() {
//
//        // Criar variaveis (SETUP)
//        UsernamePasswordAuthenticationToken dto
//                = new UsernamePasswordAuthenticationToken("a", null, Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(dto);
//
//        // Ação (ACT)
//        String idLoggedUser = usuarioService.getIdLoggedUser();
//
//
//        // Verificação (ASSERT)
//        assertEquals("a",idLoggedUser);
//    }
//
//    @Test
//    public void testarAtualizarSenhaComSucesso(){
//        final String email = "julio.gabriel@dbccompany.com.br";
//        final String senha = "abc123";
//
//
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(email);
//
//    }
//}
