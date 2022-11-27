package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public UsuarioEntity listarUsuarioEntityPeloId(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário com id " + idUsuario + " não foi encontrado."));
    }

    public List<UsuarioEntity> listarOptionalUsuarioPorEmail(String email) {
        List<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(email);
        return usuarioEntity;
    }

    public Integer getIdUsuarioLogado() {
        return Integer.parseInt(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public UsuarioDTO listarUsuarioLogado() throws Exception {
        Integer idUsuarioLogado = getIdUsuarioLogado();
        UsuarioEntity usuario = listarUsuarioEntityPeloId(idUsuarioLogado);

        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO createUsuario (UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
//        String email = usuarioCreateDTO.getEmail();
//        if(usuarioRepository.findByEmail(email).isPresent()){
//            throw new RegraDeNegocioException("E-mail já cadastrado! Realizar o cadastro novamente!");
//        }
//        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
//        usuarioEntity.setAtivo('T');
//        usuarioEntity.setSenha("123");
//        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
//        return objectMapper.convertValue(usuarioSalvo, UsuarioDTO.class);
        return null;
    }
}
