package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.entrevista.EntrevistaAtualizacaoDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaCreateDTO;
import br.com.vemser.facetoface.dto.entrevista.EntrevistaDTO;
import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.entity.enums.Legenda;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.EntrevistaRepository;
import br.com.vemser.facetoface.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;

    private final CandidatoService candidatoService;

    private final UsuarioService usuarioService;

    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    private final TokenService tokenService;

    public EntrevistaEntity findById(Integer id) throws RegraDeNegocioException {
        return entrevistaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Entrevista não encontrada!"));
    }

    public EntrevistaDTO converterParaEntrevistaDTO(EntrevistaEntity entrevistaEntity) {
        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaEntity, EntrevistaDTO.class);
        entrevistaDTO.setUsuarioDTO(usuarioService.converterEmDTO(entrevistaEntity.getUsuarioEntity()));
        entrevistaDTO.setCandidatoDTO(candidatoService.converterEmDTO(entrevistaEntity.getCandidatoEntity()));
        return entrevistaDTO;
    }

    public PageDTO<EntrevistaDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAll(pageRequest);
        List<EntrevistaDTO> entrevistaDTOList = entrevistaEntityPage.stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntityPage.getTotalElements(),
                entrevistaEntityPage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
    }

    public PageDTO<EntrevistaDTO> listMes(Integer pagina, Integer tamanho, Integer mes, Integer ano) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAllByMes(mes, ano, pageRequest);

        List<EntrevistaDTO> entrevistaDTOList = entrevistaEntityPage
                .stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntityPage.getTotalElements(),
                entrevistaEntityPage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
    }

    public EntrevistaDTO createEntrevista(EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException, MessagingException, TemplateException, IOException {
        Optional<UsuarioEntity> usuario = usuarioService.findOptionalByEmail(entrevistaCreateDTO.getUsuarioEmail());
        if (usuario.isEmpty()) {
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        CandidatoEntity candidato = candidatoService.findByEmailEntity(entrevistaCreateDTO.getCandidatoEmail());
        if (entrevistaRepository.findByCandidatoEntity(candidato).isPresent()) {
            throw new RegraDeNegocioException("Entrevista para o Candidato já agendada!");
        }
        String cidade = entrevistaCreateDTO.getCidade();
        String estado = entrevistaCreateDTO.getEstado();
        String observacoes = entrevistaCreateDTO.getObservacoes();
        LocalDateTime dia = entrevistaCreateDTO.getDataEntrevista();
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(dia);
        if (entrevistaEntityList.size() > 0) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(x -> x.getUsuarioEntity().getEmail().equals(usuario.get().getEmail()))
                    .collect(Collectors.toList());
            if (entrevistaEntityList.size() > 0) {
                throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
            }
        }
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setDataEntrevista(dia);
        entrevistaEntity.setCandidatoEntity(candidato);
        entrevistaEntity.setUsuarioEntity(usuario.get());
        entrevistaEntity.setCidade(cidade);
        entrevistaEntity.setEstado(estado);
        entrevistaEntity.setObservacoes(observacoes);
        entrevistaEntity.setLegenda(Legenda.PENDENTE);

        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevistaEntity);
        tokenConfirmacao(entrevistaSalva);
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    public void tokenConfirmacao(EntrevistaEntity entrevistaEntity) throws MessagingException, TemplateException, IOException, RegraDeNegocioException {
        String tokenSenha = tokenService.getTokenConfirmacao(entrevistaEntity);

        String base = ("Olá "
                + entrevistaEntity.getCandidatoEntity().getNomeCompleto()
                + " seu token para confirmar entrevista é é: <br>" + tokenSenha);
        emailService.sendEmailRecuperacaoSenha(entrevistaEntity.getCandidatoEntity().getEmail(), base);
    }

    public EntrevistaEntity findByCandidatoEntity(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        Optional<EntrevistaEntity> entrevistaEntityOptional = entrevistaRepository.findByCandidatoEntity(candidatoEntity);
        if (entrevistaEntityOptional.isEmpty()) {
            throw new RegraDeNegocioException("Entrevista com o candidato não encontrada!");
        }
        return entrevistaEntityOptional.get();
    }

    public void deletarEntrevista(Integer idEntrevista) throws RegraDeNegocioException {
        EntrevistaEntity entrevistaRecuperada = findById(idEntrevista);
        entrevistaRepository.delete(entrevistaRecuperada);
    }

    public EntrevistaDTO atualizarEntrevista(Integer idEntrevista, EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                             Legenda legenda) throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuario = usuarioService.findOptionalByEmail(entrevistaCreateDTO.getEmail());
        if (usuario.isEmpty()) {
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        EntrevistaEntity entrevista = findById(idEntrevista);
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(entrevista.getDataEntrevista());
        if (entrevistaEntityList.size() > 0) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(x -> x.getUsuarioEntity().getEmail().equals(usuario.get().getEmail()))
                    .collect(Collectors.toList());
            for (int i = 0; i < entrevistaEntityList.size(); i++) {
                if (entrevistaEntityList.get(i).getDataEntrevista().equals(entrevistaCreateDTO.getDataEntrevista()) &&
                        entrevistaEntityList.get(i).getUsuarioEntity().getEmail() == entrevistaCreateDTO.getEmail()) {
                    throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
                }
            }
        }
        entrevista.setCidade(entrevistaCreateDTO.getCidade());
        entrevista.setEstado(entrevistaCreateDTO.getEstado());
        entrevista.setObservacoes(entrevistaCreateDTO.getObservacoes());
        entrevista.setDataEntrevista(entrevistaCreateDTO.getDataEntrevista());
        entrevista.setLegenda(legenda);
        entrevista.setUsuarioEntity(usuario.get());
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevista);
        return converterParaEntrevistaDTO(entrevistaSalva);
    }
}