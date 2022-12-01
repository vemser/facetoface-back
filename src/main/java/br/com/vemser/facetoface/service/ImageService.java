package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.ImageEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final CandidatoService candidatoService;
    private final ImageRepository imageRepository;
    private final UsuarioService usuarioService;

    public ImageEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return imageRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public void arquivarCandidato(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImageEntity> imagemBD = findByCandidato(candidatoEntity);
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if(imagemBD.isPresent()){
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setData(file.getBytes());
            imagemBD.get().setCandidato(candidatoEntity);
            imageRepository.save(imagemBD.get());
        }else {
            ImageEntity novaImagemBD = new ImageEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setData(file.getBytes());
            novaImagemBD.setCandidato(candidatoEntity);
            imageRepository.save(novaImagemBD);
        }
    }

    public void arquivarUsuario(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntity = usuarioService.findByEmail(email);
        if(usuarioEntity.isEmpty()){
            throw new RegraDeNegocioException("Usuário não está cadastrado no sistema.");
        }
        Optional<ImageEntity> imagemBD = findByUsuario(usuarioEntity.get());
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if(imagemBD.isPresent()){
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setData(file.getBytes());
            imagemBD.get().setUsuario(usuarioEntity.get());
            imageRepository.save(imagemBD.get());
        }else {
            ImageEntity novaImagemBD = new ImageEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setData(file.getBytes());
            novaImagemBD.setUsuario(usuarioEntity.get());
            imageRepository.save(novaImagemBD);
        }
    }

    public String pegarImagemCandidato(String email) throws RegraDeNegocioException{
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(email);
        Optional<ImageEntity> imagemBD = findByCandidato(candidatoEntity);
        if (imagemBD.isEmpty()){
            throw new RegraDeNegocioException("Candidato não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getData());
    }

    public String pegarImagemUsuario(String email) throws RegraDeNegocioException{
        Optional<UsuarioEntity> usuarioEntity = usuarioService.findByEmail(email);
        if(usuarioEntity.isEmpty()){
            throw new RegraDeNegocioException("Usuário não possui imagem cadastrada.");
        }
        Optional<ImageEntity> imagemBD = findByUsuario(usuarioEntity.get());
        if (imagemBD.isEmpty()){
            throw new RegraDeNegocioException("Usuário não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getData());
    }

    private Optional<ImageEntity> findByCandidato(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        return imageRepository.findByCandidato(candidatoEntity);
    }

    private Optional<ImageEntity> findByUsuario(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        return imageRepository.findByUsuario(usuarioEntity);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        imageRepository.deleteById(id);
    }
}
