package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
//    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public UsuarioEntity auth(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                loginDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Object principal = authentication.getPrincipal();
        UsuarioEntity usuarioEntity = (UsuarioEntity) principal;
        return usuarioEntity;
    }

//    public void trocarSenha(String cpf) throws RegraDeNegocioException {
//        Optional<FuncionarioEntity> funcionarioEntity = funcionarioService.findByLogin(cpf);
//        if (funcionarioEntity.isEmpty()) {
//            throw new RegraDeNegocioException("Funcionario não existe");
//        }
//        String tokenSenha = tokenService.getTokenSenha(funcionarioEntity.get());
//        String base = "Olá " + funcionarioEntity.get().getNome() + " seu token para trocar de senha é: <br>" + tokenSenha;
//        emailService.sendEmail(base, funcionarioEntity.get().getEmail());
//    }
//
    public String procurarUsuario(String token) throws RegraDeNegocioException {
        String cpfByToken = tokenService.getEmailByToken(token);
        return usuarioService.findByEmail(cpfByToken).get().getEmail();
    }

}
