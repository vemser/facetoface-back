package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EntrevistaRepository;
import br.com.vemser.facetoface.security.TokenService;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private EmailService emailService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private EntrevistaService entrevistaService;
    @Mock
    private CandidatoService candidatoService;
    @Mock
    private EntrevistaRepository entrevistaRepository;

    @Test
    public void deveAutenticarCorretamente() {
        final String nomeEsperado = "Heloise Isabela Lopes";
        final String senha = "123";
        final String email = "heloise.lopes@dbccompany.com.br";

        UsuarioEntity usuario = getUsuarioEntity();

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setSenha(senha);

        UsernamePasswordAuthenticationToken authenticate =
                new UsernamePasswordAuthenticationToken(usuario, senha);

        when(authenticationManager.authenticate(any())).thenReturn(authenticate);

        UsuarioEntity usuarioEntity = authService.auth(loginDTO);

        assertEquals(nomeEsperado, usuarioEntity.getNomeCompleto());
        assertEquals(email, usuarioEntity.getEmail());
    }

    @Test
    public void deveConfirmarEntrevistaCorretamente() throws RegraDeNegocioException {
        final String token = "token";
        final String email = "heloise.lopes@dbccompany.com.br";

        CandidatoEntity candidatoEntity = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setLegenda(Legenda.CONFIRMADA);

        when(tokenService.getEmailByToken(token)).thenReturn(email);
        when(candidatoService.findByEmailEntity(email)).thenReturn(candidatoEntity);
        when(entrevistaService.findByCandidatoEntity(candidatoEntity)).thenReturn(entrevistaEntity);

        authService.confirmarEntrevista(token);

        verify(tokenService).getEmailByToken(any());
        verify(candidatoService).findByEmailEntity(any());
        verify(entrevistaService).findByCandidatoEntity(any());
    }

    @Test
    public void deveTrocarSenha() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        final String token = "token";
        final String email = "heloise.lopes@dbccompany.com.br";
        UsernamePasswordAuthenticationToken dto =
                new UsernamePasswordAuthenticationToken(1, email, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.findByEmail(any())).thenReturn(usuarioEntity);
        when(tokenService.getTokenSenha(any())).thenReturn(token);

        authService.trocarSenha();

        verify(usuarioService).findByEmail(any());
        verify(tokenService).getTokenSenha(any());
        verify(emailService).sendEmailRecuperacaoSenha(any(), any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoEncontrado() throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        final String email = "heloise.lopes@dbccompany.com.br";
        UsernamePasswordAuthenticationToken dto =
                new UsernamePasswordAuthenticationToken(1, email, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        when(usuarioService.findByEmail(any())).thenThrow(RegraDeNegocioException.class);
        authService.trocarSenha();
    }

    @Test
    public void deveEncontrarEmailDoUsuarioLogado() throws RegraDeNegocioException {
        final String token = "token";
        final String emailEsperado = "heloise.lopes@dbccompany.com.br";

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(tokenService.getEmailByToken(token)).thenReturn(emailEsperado);
        when(usuarioService.findByEmail(emailEsperado)).thenReturn(usuarioEntity);
        String email = authService.procurarUsuario(token);

        assertEquals(emailEsperado, email);
    }

    @Test
    public void deveEncontrarEntrevistaCandidato() throws RegraDeNegocioException {
        final int idEsperado = 1;
        final String cidadeEsperado = "Santana";
        final String estadoEsperado = "AP";

        final String token = "token";
        final String email = "heloise.lopes@dbccompany.com.br";

        CandidatoEntity candidatoEntity = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(tokenService.getEmailByToken(token)).thenReturn(email);
        when(candidatoService.findByEmailEntity(email)).thenReturn(candidatoEntity);
        when(entrevistaService.findByCandidatoEntity(candidatoEntity)).thenReturn(entrevistaEntity);

        EntrevistaEntity entrevista = authService.procurarCandidato(token);

        assertEquals(idEsperado, entrevista.getIdEntrevista());
        assertEquals(cidadeEsperado, entrevista.getCidade());
        assertEquals(estadoEsperado, entrevista.getEstado());
    }

    private static CandidatoEntity getCandidatoEntity() {
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setGenero(Genero.FEMININO);
        candidatoEntity.setAtivo('T');

        return candidatoEntity;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNomeCompleto("Heloise Isabela Lopes");
        usuarioEntity.setCidade("Santana");
        usuarioEntity.setEstado("AP");
        usuarioEntity.setEmail("heloise.lopes@dbccompany.com.br");
        usuarioEntity.setGenero(Genero.FEMININO);
        usuarioEntity.setAtivo('T');

        return usuarioEntity;
    }

    private static EntrevistaEntity getEntrevistaEntity() {
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setIdEntrevista(1);
        entrevistaEntity.setDataEntrevista(LocalDateTime.now().plusDays(1));
        entrevistaEntity.setCidade("Santana");
        entrevistaEntity.setEstado("AP");
        entrevistaEntity.setLegenda(Legenda.PENDENTE);

        return entrevistaEntity;
    }
}
