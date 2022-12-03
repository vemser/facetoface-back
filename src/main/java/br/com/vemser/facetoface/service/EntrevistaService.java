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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public EntrevistaDTO createEntrevista(EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuario = usuarioService.findByEmail(entrevistaCreateDTO.getUsuarioEmail());
        CandidatoEntity candidato = candidatoService.findByEmailEntity(entrevistaCreateDTO.getCandidatoEmail());
        if (entrevistaRepository.findByCandidatoEntity(candidato).isPresent()) {
            throw new RegraDeNegocioException("Entrevista para o Candidato já agendada!");
        }
        String cidade = entrevistaCreateDTO.getCidade();
        String estado = entrevistaCreateDTO.getEstado();
        String observacoes = entrevistaCreateDTO.getObservacoes();
        LocalDateTime dia = entrevistaCreateDTO.getDataEntrevista();
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(dia);
        verificarListaEntrevistas(entrevistaCreateDTO, usuario, entrevistaEntityList);
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setDataEntrevista(dia);
        entrevistaEntity.setCandidatoEntity(candidato);
        entrevistaEntity.setUsuarioEntity(usuario);
        entrevistaEntity.setCidade(cidade);
        entrevistaEntity.setEstado(estado);
        entrevistaEntity.setObservacoes(observacoes);
        entrevistaEntity.setLegenda(Legenda.PENDENTE);
        tokenConfirmacao(entrevistaEntity);
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevistaEntity);
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    public void tokenConfirmacao(EntrevistaEntity entrevistaEntity) throws RegraDeNegocioException {
        String tokenSenha = tokenService.getTokenConfirmacao(entrevistaEntity);

        emailService.sendEmailConfirmacaoEntrevista(entrevistaEntity, entrevistaEntity.getCandidatoEntity().getEmail(), tokenSenha);
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
        UsuarioEntity usuario = usuarioService.findByEmail(entrevistaCreateDTO.getEmail());
//        if (usuario.isEmpty()) {
//            throw new RegraDeNegocioException("Usuário não encontrado");
//        }
        EntrevistaEntity entrevista = findById(idEntrevista);
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(entrevista.getDataEntrevista());
        verificarListaEntrevistas(entrevistaCreateDTO, usuario, entrevistaEntityList);
        entrevista.setCidade(entrevistaCreateDTO.getCidade());
        entrevista.setEstado(entrevistaCreateDTO.getEstado());
        entrevista.setObservacoes(entrevistaCreateDTO.getObservacoes());
        entrevista.setDataEntrevista(entrevistaCreateDTO.getDataEntrevista());
        entrevista.setLegenda(legenda);
        entrevista.setUsuarioEntity(usuario);
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevista);
        if(entrevista.getDataEntrevista().isAfter(LocalDateTime.now()) && legenda.equals(Legenda.PENDENTE)){
            tokenConfirmacao(entrevista);
        }
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    private void verificarListaEntrevistas(EntrevistaAtualizacaoDTO entrevistaCreateDTO, UsuarioEntity usuario, List<EntrevistaEntity> entrevistaEntityList) throws RegraDeNegocioException {
        if (!entrevistaEntityList.isEmpty()) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(x -> x.getUsuarioEntity().getEmail().equals(usuario.getEmail()))
                    .collect(Collectors.toList());
            for (EntrevistaEntity entrevistaEntity : entrevistaEntityList) {
                if (entrevistaEntity.getDataEntrevista().equals(entrevistaCreateDTO.getDataEntrevista()) &&
                        entrevistaEntity.getUsuarioEntity().getEmail() == entrevistaCreateDTO.getEmail()) {
                    throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
                }
            }
        }
    }

    private void verificarListaEntrevistas(EntrevistaCreateDTO entrevistaCreateDTO, UsuarioEntity usuario, List<EntrevistaEntity> entrevistaEntityList) throws RegraDeNegocioException {
        if (!entrevistaEntityList.isEmpty()) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(x -> x.getUsuarioEntity().getEmail().equals(usuario.getEmail()))
                    .collect(Collectors.toList());
            for (EntrevistaEntity entrevistaEntity : entrevistaEntityList) {
                if (entrevistaEntity.getDataEntrevista().equals(entrevistaCreateDTO.getDataEntrevista())) {
                    throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
                }
            }
        }
    }

    public void atualizarObservacaoEntrevista(Integer id, String observacao) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = findById(id);
        entrevista.setObservacoes(observacao);
        entrevistaRepository.save(entrevista);
    }
}
