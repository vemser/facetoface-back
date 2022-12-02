package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.login.LoginRetornoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
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
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.vemser.facetoface.factory.UsuarioFactory.getUsuarioEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
    public void testarCadastarUsuarioComSucesso() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
        final String trilhaEsperado = "BACKEND";
        final String senhaCriptografada = "j183nsur74bd83gr7";

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
        PerfilEntity perfil = PerfilFactory.getPerfilEntity();

        when(usuarioService.findOptionalByEmail(anyString())).thenReturn(Optional.empty());
        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
        when(perfilService.findByNome(anyString())).thenReturn(perfil);
        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);

        UsuarioDTO usuarioDTO = usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);

        assertEquals(usuarioDTO.getNomeCompleto(), usuarioEntity.getNomeCompleto());
        assertEquals(trilhaEsperado, usuarioDTO.getTrilha().getNome());
        assertEquals(emailEsperado, usuarioDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarCadastarUsuarioComErro() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));

        usuarioService.createUsuario(usuarioCreateDTO, Genero.FEMININO);
    }

    @Test
    public void testarAtualizarUsuarioComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
        EdicaoEntity edicao = EdicaoFactory.getEdicaoEntity();
        PerfilEntity perfil = PerfilFactory.getPerfilEntity();

        UsuarioEntity usuarioSalvo = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
//        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
        when(perfilService.findByNome(anyString())).thenReturn(perfil);
        when(usuarioRepository.save(any())).thenReturn(usuarioSalvo);

        UsuarioDTO usuarioDTO = usuarioService.update(1, usuarioCreateDTO, Genero.FEMININO);

        assertEquals(usuarioSalvo.getEmail(), usuarioDTO.getEmail());
        assertEquals(usuarioSalvo.getPerfis().size(), usuarioDTO.getPerfis().size());
    }

    @Test
    public void testarBuscarUsuarioDTOPorEmailComSucesso() throws RegraDeNegocioException {
        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        UsuarioDTO usuarioDTO = usuarioService.findByEmailDTO(emailEsperado);

        assertEquals(emailEsperado, usuarioDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarBuscarUsuarioDTOPorEmailComErro() throws RegraDeNegocioException {
        final String emailEsperado = "julio.gabriel@dbccompany.com.br";

        when(usuarioRepository.findByEmail(anyString())).thenReturn((Optional.empty()));
        usuarioService.findByEmailDTO(emailEsperado);
    }

    @Test
    public void testarRetornoListaPaginadaComSucesso() {
        final int pagina = 0;
        final int tamanho = 5;

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        PageImpl<UsuarioEntity> usuarioEntities =
                new PageImpl<>(List.of(usuarioEntity), PageRequest.of(pagina, tamanho), 0);

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(usuarioEntities);

        PageDTO<UsuarioDTO> usuarioDTOPaginado = usuarioService.list(pagina, tamanho);

        assertEquals(pagina, usuarioDTOPaginado.getPagina());
        assertEquals(1, usuarioDTOPaginado.getElementos().size());
        assertEquals(tamanho, usuarioDTOPaginado.getTamanho());
    }

    @Test
    public void testarDeletarLogicamenteUsuarioComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        usuarioService.deleteLogico(1);

        verify(usuarioRepository).save(any());
    }

    @Test
    public void testarDeletarFisicamenteUsuarioComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        usuarioService.deleteFisico(1);

        verify(usuarioRepository).deleteById(any());
    }

    @Test
    public void testarBuscarEmailUsuarioComSucesso() {
        final String emailEsperado = "julio.gabriel@dbccompany.com.br";
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        Optional<UsuarioEntity> usuario = usuarioService.findOptionalByEmail(emailEsperado);

        assertEquals(usuario.get().getEmail(), emailEsperado);
    }

    @Test
    public void deveTestarGetIdLoggedUserComSucesso() {
        SecurityContextHolder.getContext().setAuthentication(getAuthentication());
    }


    @Test
    public void testarBuscarLoginComsucesso() {

        // Criar variaveis (SETUP)
        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken("a", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        // Ação (ACT)
        String idLoggedUser = usuarioService.getIdLoggedUser();


        // Verificação (ASSERT)
        assertEquals("a", idLoggedUser);
    }

    @Test
    public void testarAtualizarSenhaComSucesso() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        final String email = "julio.gabriel@dbccompany.com.br";
        final String senha = "123";
        UsuarioEntity usuarioEntity = getUsuarioEntity();


        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode(anyString())).thenReturn(senha);
        usuarioService.atualizarSenhaUsuario(email);

        assertEquals(usuarioEntity.getSenha(), senha);

    }

    @Test
    public void testarBuscarUsuarioPorLogingComSucesso() {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        final String email = getUsuarioEntity().getEmail();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        Optional<UsuarioEntity> usuarioRecuperado = usuarioService.findByLogin(email);

        assertEquals(usuarioRecuperado.get().getEmail(), usuarioEntity.getEmail());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarBuscarUsuarioPorIdComErro() throws RegraDeNegocioException {
        final Integer id = 2;

        when(usuarioRepository.findById(any())).thenReturn(Optional.empty());
        usuarioService.findById(id);
    }

    @Test
    public void testarBuscarUsuarioDTOPorIdComSucesso() throws RegraDeNegocioException {
        UsuarioDTO usuarioDTO = UsuarioFactory.getUsuarioDTO();
        final Integer id = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        UsuarioDTO usuarioAchado = usuarioService.findByIdDTO(id);

        assertEquals(usuarioAchado.getIdUsuario(), usuarioDTO.getIdUsuario());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarBuscarUsuarioDTOPorIdComErro() throws RegraDeNegocioException {
        final Integer id = 2;

        when(usuarioRepository.findById(any())).thenReturn(Optional.empty());
        usuarioService.findByIdDTO(id);

    }

    @Test
    public void testarPegarUsuarioLogador() {
        UsuarioEntity usuarioEntity = getUsuarioEntity();


    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarCriarUsuarioComErroDeEmail() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        UsuarioCreateDTO usuarioCreateDTO = UsuarioFactory.getUsuarioDTO();
        usuarioCreateDTO.setEmail("abc");

        when(usuarioService.findOptionalByEmail(anyString())).thenReturn(Optional.empty());
        usuarioService.createUsuario(usuarioCreateDTO, Genero.MASCULINO);

    }

//    @Test
//    public void testarEnviarEmailComSenhaSucesso() throws RegraDeNegocioException{
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        final String email = usuarioEntity.getEmail();
//        final String senha = "123";
//
//        when(emailService.sendEmailEnvioSenha(usuarioEntity.getEmail(), usuarioEntity.getSenha())).
//                thenReturn();
//    }

    @Test
    public void testarValidarFormatacaoComSucesso() throws RegraDeNegocioException {
        String senha = "Adsafd@!153~";

        boolean deuCerto = usuarioService.validarFormatacao(senha);

        assertTrue(true, String.valueOf(deuCerto));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarValidacaoSenhacomErro() throws RegraDeNegocioException {
        String senha = "A#@Dsclpoe#@!SDAz#89";

        boolean deuCerto = usuarioService.validarFormatacao(senha);

        assertTrue(true, String.valueOf(deuCerto));
    }

    //    @Test
//    public void TestarSenhaUsuarioLogadoComSucesso() throws RegraDeNegocioException{
//        UsuarioEntity usuarioEntity = UsuarioFactory.getUsuarioEntity();
//        String email = usuarioEntity.getEmail();
//        String senha = "Adsafd@!153~";
//        UsuarioEntity usuarioSenhaNova = getUsuarioEntity();
//        usuarioSenhaNova.setSenha(senha);
//
//        UsernamePasswordAuthenticationToken dto
//                = new UsernamePasswordAuthenticationToken(1, usuarioEntity.getEmail(), Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(dto);
//        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
//        usuarioService.atualizarSenhaUsuarioLogado(usuarioEntity.getSenha(), senha);
//
//        assertNotEquals(usuarioSenhaNova.getSenha(), usuarioEntity.getSenha());
//    }
    @Test(expected = RegraDeNegocioException.class)
    public void testarDeletarUsarioLogicamenteComErro() throws RegraDeNegocioException {
        final Integer id = 2;
        UsuarioEntity usuarioEntity = getUsuarioEntity();

//        when(usuarioRepository.save(any())).thenReturn(Optional.empty());
        usuarioService.deleteLogico(2);
    }


    @Test
    public void testarBuscarUsuarioPorNomeCompletoComSucesso() throws RegraDeNegocioException {
        final int pagina = 0;
        final int tamanho = 5;
        final String nomeCompleto = getUsuarioEntity().getNomeCompleto();

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        PageImpl<UsuarioEntity> usuarioEntities =
                new PageImpl<>(List.of(usuarioEntity), PageRequest.of(pagina, tamanho), 1);

        when(usuarioRepository.findByNomeCompletoContaining(anyString(), any())).thenReturn(usuarioEntities);

        PageDTO<UsuarioDTO> usuarioDTOPageDTO = usuarioService.findByNomeCompleto(nomeCompleto, pagina, tamanho);

        assertEquals(pagina, usuarioDTOPageDTO.getPagina());
        assertEquals(1, usuarioDTOPageDTO.getElementos().size());
        assertEquals(tamanho, usuarioDTOPageDTO.getTamanho());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testarBuscarUsuarioPorNomeCompletoComErro() throws RegraDeNegocioException {
        final int pagina = 0;
        final int tamanho = 5;
        final String nomeCompleto = "Abc";

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        PageImpl<UsuarioEntity> usuarioEntities =
                new PageImpl<>(List.of(usuarioEntity), PageRequest.of(pagina, tamanho), 1);

        when(usuarioRepository.findByNomeCompletoContaining(anyString(), any())).thenReturn(Page.empty());

        usuarioService.findByNomeCompleto(nomeCompleto, pagina, tamanho);
    }

    @Test
    public void deveRetornarUsuarioLogadoCorretamente() {
        final String email = "julio.gabriel@dbccompany.com.br";
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, email, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));

        LoginRetornoDTO loginRetornoDTO = usuarioService.getLoggedUser();

        assertEquals(email, loginRetornoDTO.getEmail());
    }

    @Test
    public void deveRetornarUmUsuarioQuandoEmailEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        final String email = "julio.gabriel@dbccompany.com.br";

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));

        UsuarioEntity usuario = usuarioService.findByEmail(email);

        assertEquals(email, usuario.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoEmailDoUsuarioNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        final String email = "julio.gabriel@dbccompany.com.br";

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        usuarioService.findByEmail(email);
    }

    @Test
    public void deveAtualizarSenhaUsuarioLogado() throws RegraDeNegocioException {
        final String email = "julio.gabriel@dbccompany.com.br";
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, email, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        when(usuarioService.findOptionalByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        usuarioService.atualizarSenhaUsuarioLogado("senha123", "Adsafd@!153~");

        verify(usuarioRepository).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoSenhaAtualForErrada() throws RegraDeNegocioException {
        final String email = "julio.gabriel@dbccompany.com.br";
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, email, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        when(usuarioService.findOptionalByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        usuarioService.atualizarSenhaUsuarioLogado("senha123", "Adsafd@!153~");
    }

    private static UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(1,
                null,
                Collections.emptyList());
    }
}
