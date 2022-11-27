package br.com.vemser.facetoface.security;

import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
            Optional<UsuarioEntity> funcionarioEntityOptional = usuarioService.listarOptionalUsuarioPorEmail(username);
            return funcionarioEntityOptional
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário inválido"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
        }
    }

