package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.PerfilDTO;
import br.com.vemser.facetoface.dto.login.LoginDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import br.com.vemser.facetoface.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final TrilhaService trilhaService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário com id " + idUsuario + " não foi encontrado."));
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Integer getIdUsuarioLogado() {
        return Integer.parseInt(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public UsuarioDTO listarUsuarioLogado() throws RegraDeNegocioException {
        Integer idUsuarioLogado = getIdUsuarioLogado();
        UsuarioEntity usuario = findById(idUsuarioLogado);

        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

//    public String auth(LoginDTO loginDTO) {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        loginDTO.getEmail(),
//                        loginDTO.getSenha()
//                );
//        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//        Object principal = authenticate.getPrincipal();
//        UsuarioEntity usuarioEntity = (UsuarioEntity) principal;
//
//        return tokenService.getToken(usuarioEntity, null);
//    }

//    public UsuarioDTO createAdmin(UsuarioCreateDTO usuarioCreateDTO, Integer idPerfil) throws RegraDeNegocioException {
//        List<PerfilEntity> perfilEntityList = perfilService.listarPerfis();
//
//        UsuarioEntity usuario = createUsuario(usuarioCreateDTO);
//        usuario.setPerfis(Set.copyOf(perfilEntityList));
//
//        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);
//        return objectMapper.convertValue(usuarioSalvo, UsuarioDTO.class);
//    }

    public UsuarioDTO createUsuario(UsuarioCreateDTO usuarioCreateDTO, int idPerfil, int idTrilha, Genero genero) throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuario = findByEmail(usuarioCreateDTO.getEmail());

        if (usuario.isPresent()) {
            throw new RegraDeNegocioException("Usuário já cadastrado com o mesmo email.");
        }

        PerfilEntity perfilEntity = perfilService.findById(idPerfil);
        TrilhaEntity trilhaEntity = trilhaService.findById(idTrilha);

        String senha = "123";
        String senhaEncode = passwordEncoder.encode(senha);

        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
        usuarioEntity.setPerfis(Set.of(perfilEntity));
        usuarioEntity.setSenha(senhaEncode);
        usuarioEntity.setTrilha(trilhaEntity);
        usuarioEntity.setGenero(genero);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
//        Set<PerfilEntity> perfilEntities = usuarioSalvo.getPerfis();

//        List<PerfilDTO> perfilDTOS = perfilEntities.stream()
//                .map(perfilService::convertToDTO)
//                .toList();

        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioSalvo, UsuarioDTO.class);
        usuarioDTO.setPerfilEntities(usuarioSalvo.getPerfis());
        usuarioDTO.setTrilhaEntity(usuarioSalvo.getTrilha());

        return usuarioDTO;
    }
}
