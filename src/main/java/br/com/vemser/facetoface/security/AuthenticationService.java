//package br.com.vemser.facetoface.security;
//
//import br.com.dbc.vemser.sistemaaluguelveiculos.entity.FuncionarioEntity;
//import br.com.dbc.vemser.sistemaaluguelveiculos.service.FuncionarioService;
//import br.com.vemser.facetoface.entity.UsuarioEntity;
//import br.com.vemser.facetoface.service.UsuarioService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService implements UserDetailsService {
//
//    private final UsuarioService usuarioService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UsuarioEntity> usuarioEntityOptional = usuarioService.findByLogin(username);
//        return usuarioEntityOptional
//                .orElseThrow(() -> new UsernameNotFoundException("Usuário inválido"));
//    }
//}
