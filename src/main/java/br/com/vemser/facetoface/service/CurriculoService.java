package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.CurriculoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CurriculoRepository;
import br.com.vemser.facetoface.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository curriculoRepository;

    private final CandidatoService candidatoService;

    public CurriculoEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return curriculoRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public CurriculoEntity arquivarCurriculo(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
        CandidatoDTO candidato = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity = candidatoService.findById(candidato.getIdCandidato());
        String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
        CurriculoEntity curriculo = new CurriculoEntity();
        curriculo.setNome(nomeArquivo);
        curriculo.setTipo(file.getContentType());
        curriculo.setDado(file.getBytes());
        curriculo.setCandidato(candidatoEntity);
        return curriculoRepository.save(curriculo);
    }

    public String pegarCurriculoCandidato(String email) throws RegraDeNegocioException{
        CandidatoDTO candidato = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity = candidatoService.findById(candidato.getIdCandidato());
        CurriculoEntity curriculo = curriculoRepository.findByCandidato(candidatoEntity);
        return Base64Utils.encodeToString(curriculo.getDado());
    }
}
