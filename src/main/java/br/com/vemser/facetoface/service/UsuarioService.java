package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.dto.PerfilDTO;
import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.*;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import br.com.vemser.facetoface.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public UsuarioDTO createUsuario(UsuarioCreateDTO usuarioCreateDTO, Genero genero) throws RegraDeNegocioException {
        List<PerfilEntity> perfilEntityList = new ArrayList<>();
        Optional<UsuarioEntity> usuario = findByEmail(usuarioCreateDTO.getEmail());

        if (usuario.isPresent()) {
            throw new RegraDeNegocioException("Usuário já cadastrado com o mesmo email.");
        }

        String senha = "123";
        String senhaEncode = passwordEncoder.encode(senha);
        for (PerfilDTO perfilDTO : usuarioCreateDTO.getPerfis()) {
            PerfilEntity byNome = perfilService.findByNome(perfilDTO.getNome());
            perfilEntityList.add(byNome);
        }

        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
        usuarioEntity.setSenha(senhaEncode);
        usuarioEntity.setTrilha(trilhaService.findByNome(usuarioCreateDTO.getTrilha().getNome()));
        usuarioEntity.setPerfis(perfilEntityList);
        usuarioEntity.setGenero(genero);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
//        Set<PerfilEntity> perfilEntities = usuarioSalvo.getPerfis();

//        List<PerfilDTO> perfilDTOS = perfilEntities.stream()
//                .map(perfilService::convertToDTO)
//                .toList();
        return converterEmDTO(usuarioSalvo);
    }

    public PageDTO<UsuarioDTO> findByNomeCompleto(String nomeCompleto, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("nome_completo");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findByNomeCompleto(nomeCompleto, pageRequest);
        if(usuarioEntityPage.isEmpty()){
            throw new RegraDeNegocioException("Usuário com o nome especificado não existe");
        }
        List<UsuarioDTO> usuarioDTOList = usuarioRepository.findAll()
                .stream()
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(usuarioEntityPage.getTotalElements(),
                usuarioEntityPage.getTotalPages(),
                pagina,
                tamanho,
                usuarioDTOList);
    }

    private UsuarioDTO converterEmDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        usuarioDTO.setTrilha(objectMapper.convertValue(usuarioEntity.getTrilha(), TrilhaDTO.class));
        usuarioDTO.setPerfis(usuarioEntity.getPerfis()
                .stream()
                .map(perfil -> objectMapper.convertValue(perfil, PerfilDTO.class))
                .collect(Collectors.toList()));
        return usuarioDTO;
    }
}
