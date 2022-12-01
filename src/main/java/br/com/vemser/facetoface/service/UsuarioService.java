package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.PerfilDTO;
import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.dto.login.LoginRetornoDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.facetoface.dto.usuario.UsuarioDTO;
import br.com.vemser.facetoface.entity.PerfilEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final TrilhaService trilhaService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário com id " + idUsuario + " não foi encontrado."));
    }

    public UsuarioDTO findByIdDTO(Integer idUsuario) throws RegraDeNegocioException {
        return converterEmDTO(findById(idUsuario));
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioDTO findByEmailDTO(String email) throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(email);
        if (usuarioEntity.isEmpty()) {
            throw new RegraDeNegocioException("Usuário com o e-mail especificado não existe");
        }
        return converterEmDTO(usuarioEntity.get());
    }

    public Optional<UsuarioEntity> findByLogin(String email) {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(email);
        return usuarioEntity;
    }

    public String getIdLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    public LoginRetornoDTO getLoggedUser() {
        Optional<UsuarioEntity> usuarioEntity = findByLogin(getIdLoggedUser());
        LoginRetornoDTO loginDTO = objectMapper.convertValue(usuarioEntity.get(), LoginRetornoDTO.class);
        loginDTO.setPerfis(usuarioEntity.get().getPerfis().stream().map(perfilService::convertToDTO).toList());
        return loginDTO;
    }

    public UsuarioDTO createUsuario(UsuarioCreateDTO usuarioCreateDTO, Genero genero) throws RegraDeNegocioException {
        if (!usuarioCreateDTO.getEmail().endsWith("@dbccompany.com.br") || usuarioCreateDTO.getEmail().isEmpty()) {
            throw new RegraDeNegocioException("E-mail inválido! Deve ser domínio @dbccompany.com.br");
        }
        Set<PerfilEntity> perfilEntityList = new HashSet<>();
        Optional<UsuarioEntity> usuario = findByEmail(usuarioCreateDTO.getEmail());
        Faker faker = new Faker();
        if (usuario.isPresent()) {
            throw new RegraDeNegocioException("Usuário já cadastrado com o mesmo email.");
        }
        String senha = faker.internet().password(8, 12, true, true, true);
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
        usuarioEntity.setNomeCompleto(usuarioEntity.getNomeCompleto().trim());
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        emailService.sendEmailEnvioSenha(usuarioSalvo.getEmail(), senha);

        return converterEmDTO(usuarioSalvo);
    }

    public void deleteLogico(Integer id) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = findById(id);
        usuarioEntity.setAtivo('F');
        usuarioRepository.save(usuarioEntity);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = findById(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO update(Integer id, UsuarioCreateDTO usuarioCreateDTO, Genero genero) throws RegraDeNegocioException {
        Set<PerfilEntity> perfilEntityList = new HashSet<>();
        UsuarioEntity usuarioEntitySenha = findById(id);
        UsuarioEntity usuarioEntity = converterEntity(usuarioCreateDTO);
        for (PerfilDTO perfilDTO : usuarioCreateDTO.getPerfis()) {
            PerfilEntity byNome = perfilService.findByNome(perfilDTO.getNome());
            perfilEntityList.add(byNome);
        }
        usuarioEntity.setSenha(usuarioEntitySenha.getSenha());
        usuarioEntity.setIdUsuario(id);
        usuarioEntity.setTrilha(trilhaService.findByNome(usuarioCreateDTO.getTrilha().getNome()));
        usuarioEntity.setPerfis(perfilEntityList);
        usuarioEntity.setGenero(genero);
        usuarioEntity.setNomeCompleto(usuarioEntity.getNomeCompleto().trim());
        return converterEmDTO(usuarioRepository.save(usuarioEntity));
    }

    public PageDTO<UsuarioDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findAll(pageRequest);
        List<UsuarioDTO> usuarioDTOList = usuarioEntityPage
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(usuarioEntityPage.getTotalElements(),
                usuarioEntityPage.getTotalPages(),
                pagina,
                tamanho,
                usuarioDTOList);
    }

    public PageDTO<UsuarioDTO> findByNomeCompleto(String nomeCompleto, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("nomeCompleto");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findByNomeCompleto(nomeCompleto.trim(), pageRequest);
        if (usuarioEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Usuário com o nome especificado não existe");
        }
        List<UsuarioDTO> usuarioDTOList = usuarioEntityPage
                .stream()
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(usuarioEntityPage.getTotalElements(),
                usuarioEntityPage.getTotalPages(),
                pagina,
                tamanho,
                usuarioDTOList);
    }

    private UsuarioEntity converterEntity(UsuarioCreateDTO usuarioCreateDTO) {
        return objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
    }

    public UsuarioDTO converterEmDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        usuarioDTO.setTrilha(objectMapper.convertValue(usuarioEntity.getTrilha(), TrilhaDTO.class));
        usuarioDTO.setPerfis(usuarioEntity.getPerfis()
                .stream()
                .map(perfil -> objectMapper.convertValue(perfil, PerfilDTO.class))
                .collect(Collectors.toList()));
        return usuarioDTO;
    }

    public void atualizarSenhaUsuario(String email, String senha) {
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioRepository.findByEmail(email);
        usuarioEntityOptional.get().setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuarioEntityOptional.get());
    }
}
