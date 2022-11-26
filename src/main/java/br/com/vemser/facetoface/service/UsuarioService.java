package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public UsuarioEntity listarUsuarioEntityPeloId(Integer idUsuario) throws Exception {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuário com id " + idUsuario + " não foi encontrado."));
    }

    public Optional<UsuarioEntity> listarOptionalUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Integer getIdUsuarioLogado() {
        return Integer.parseInt(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public UsuarioDTO listarUsuarioLogado() throws Exception {
        Integer idUsuarioLogado = getIdUsuarioLogado();
        UsuarioEntity usuario = listarUsuarioEntityPeloId(idUsuarioLogado);

        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }
    
}
