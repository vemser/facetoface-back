package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EntrevistaRepository;
import br.com.vemser.facetoface.security.TokenService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EntrevistaService entrevistaService;
    private final CandidatoService candidatoService;
    private final EntrevistaRepository entrevistaRepository;

    public UsuarioEntity auth(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getSenha()
                );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Object principal = authentication.getPrincipal();
        UsuarioEntity usuarioEntity = (UsuarioEntity) principal;
        return usuarioEntity;
    }

    public void confirmarEntrevista(String token) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = procurarCandidato(token);
        entrevista.setLegenda(Legenda.CONFIRMADA);
        entrevistaRepository.save(entrevista);
    }

    public void trocarSenha(String email) throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        UsuarioEntity usuarioEntityOptional = usuarioService.findByEmail(email);

        String tokenSenha = tokenService.getTokenSenha(usuarioEntityOptional);
        String base = "Olá "
                + usuarioEntityOptional.getNomeCompleto()
                + " seu token para trocar de senha é: <br>"
                + tokenSenha;
        emailService.sendEmailRecuperacaoSenha(email, base);
    }

    public String procurarUsuario(String token) throws RegraDeNegocioException {
        String cpfByToken = tokenService.getEmailByToken(token);
        return usuarioService.findByEmail(cpfByToken).getEmail();
    }

    public EntrevistaEntity procurarCandidato(String token) throws RegraDeNegocioException {
        String emailCandidatoByToken = tokenService.getEmailByToken(token);
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(emailCandidatoByToken);
        return entrevistaService.findByCandidatoEntity(candidatoEntity);
    }
}
