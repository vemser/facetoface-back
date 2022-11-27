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
import java.util.Optional;

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
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImageEntity> imagemBD = findByCandidato(candidatoEntity);
        String nomeArquivo = StringUtils.cleanPath((Objects.requireNonNull(file.getOriginalFilename())));
        if(imagemBD.isPresent()){
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setData(file.getBytes());
            imagemBD.get().setCandidato(candidatoEntity);
            return imageRepository.save(imagemBD.get());
        }
        ImageEntity novaImagemBD = new ImageEntity();
        novaImagemBD.setNome(nomeArquivo);
        novaImagemBD.setTipo(file.getContentType());
        novaImagemBD.setData(file.getBytes());
        novaImagemBD.setCandidato(candidatoEntity);
        return imageRepository.save(novaImagemBD);
    }

    public String pegarImagemUsuario(String email) throws RegraDeNegocioException{
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImageEntity> imagemBD = findByCandidato(candidatoEntity);
        if (imagemBD.isEmpty()){
            throw new RegraDeNegocioException("Usuário não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getData());
    }

    private Optional<ImageEntity> findByCandidato(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        return imageRepository.findByCandidato(candidatoEntity);
    }
}
