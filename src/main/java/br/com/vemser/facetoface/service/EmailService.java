package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.entity.EntrevistaEntity;
import br.com.vemser.facetoface.entity.UsuarioEntity;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public void sendEmailEnvioSenha(UsuarioEntity usuarioEntity, String senha) throws RegraDeNegocioException {
        String subject = "Cadastro concluído com sucesso.";
        sendEmail(usuarioEntity, senha, "envio-senha-template.html", subject);
    }

    public void sendEmailRecuperacaoSenha(UsuarioEntity usuarioEntity, String token) throws RegraDeNegocioException {
        final String subject = "Recuperação de senha concluída com sucesso.";
        sendEmailRecuperacaoSenha(usuarioEntity, token, "envio-senha-recuperacao.html", subject);
    }

    public void sendEmail(UsuarioEntity usuarioEntity, String info, String nomeTemplate, String assunto) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuarioEntity.getEmail());
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplateEnvioSenha(info, nomeTemplate, usuarioEntity), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RegraDeNegocioException("Email inválido! inserir outro e-mail.");
        }
    }

    public void sendEmailRecuperacaoSenha(UsuarioEntity usuarioEntity, String info, String nomeTemplate, String assunto) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuarioEntity.getEmail());
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplateRecuperarSenha(info, nomeTemplate, usuarioEntity), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RegraDeNegocioException("Email inválido! inserir outro e-mail.");
        }
    }

    public void sendEmailConfirmacaoEntrevista(EntrevistaEntity entrevistaEntity, String email, String token) throws RegraDeNegocioException {
        final String subject = "Confirmação de entrevista.";
        sendEmailEntrevista(entrevistaEntity, email, token, "envio-entrevista-template.html", subject);
    }

    public void sendEmailEntrevista(EntrevistaEntity entrevistaEntity, String email, String token, String nomeTemplate, String assunto) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplateEntrevista(entrevistaEntity.getDataEntrevista(), entrevistaEntity.getCandidatoEntity().getNomeCompleto(), nomeTemplate, token), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RegraDeNegocioException("Email inválido! inserir outro e-mail.");
        }
    }

    public String getContentFromTemplateEntrevista(LocalDateTime data, String nome, String nomeTemplate, String token) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("nome", nome);
        dados.put("data", data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        dados.put("token", "https://facetoface-front.vercel.app/confirm-interview?token="+token);
        dados.put("colaborador", from);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getContentFromTemplateEnvioSenha(String info, String nomeTemplate, UsuarioEntity usuarioEntity) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("nome", usuarioEntity.getNomeCompleto());
        dados.put("senha", info);
        dados.put("login", usuarioEntity.getEmail());
        dados.put("colaborador", from);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getContentFromTemplateRecuperarSenha(String info, String nomeTemplate, UsuarioEntity usuarioEntity) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("nome", usuarioEntity.getNomeCompleto());
        dados.put("senha", "https://facetoface-front.vercel.app/recover-password-token?token="+info);
        dados.put("colaborador", from);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }
}
