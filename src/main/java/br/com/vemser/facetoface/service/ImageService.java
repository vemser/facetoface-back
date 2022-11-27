package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.CandidatoRepository;
import br.com.vemser.facetoface.repository.ImageRepository;
import br.com.vemser.facetoface.repository.TrilhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final CandidatoService candidatoService;
    private final ImageRepository imageRepository;

    public ImageEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return imageRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

//    public ImageEntity arquivarUsuario(MultipartFile file, String email) throws IOException, RegraDeNegocioException{
//        UsuarioEntity usuario = usuarioService.findByEmail(email);
//
//
//    }

    public ImageEntity arquivarCandidato(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
        CandidatoDTO candidato = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity = candidatoService.findById(candidato.getIdCandidato());
        String nomeArquivo = StringUtils.cleanPath((Objects.requireNonNull(file.getOriginalFilename())));
        ImageEntity imagemBD = new ImageEntity();
        imagemBD.setNome(nomeArquivo);
        imagemBD.setTipo(file.getContentType());
        imagemBD.setData(file.getBytes());
        imagemBD.setCandidato(candidatoEntity);
        return imageRepository.save(imagemBD);
    }

    public String pegarImagemUsuario(String email) throws RegraDeNegocioException{
        CandidatoDTO candidato = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity = candidatoService.findById(candidato.getIdCandidato());
        ImageEntity imagemBD = imageRepository.findByCandidato(candidatoEntity);
        return Base64Utils.encodeToString(imagemBD.getData());
    }
}
