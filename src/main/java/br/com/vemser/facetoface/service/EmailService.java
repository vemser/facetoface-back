package br.com.vemser.facetoface.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

//    private final freemarker.template.Configuration fmConfiguration;
//    @Value("${spring.mail.username}")
//    private String from;
//    private final JavaMailSender emailSender;
//
//    public void sendEmail(UsuarioDTO usuarioDTO, TipoEmails tipoEmails, String token) {
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom(from);
//            mimeMessageHelper.setTo(usuarioDTO.getEmail());
//            mimeMessageHelper.setSubject(tipoEmails.getDescricao());
//            if(token == null){
//                mimeMessageHelper.setText(geContentFromTemplate(usuarioDTO, tipoEmails), true);
//                emailSender.send(mimeMessageHelper.getMimeMessage());
//            } else {
//                mimeMessageHelper.setText(geContentFromTemplateToken(usuarioDTO, tipoEmails, token), true);
//                emailSender.send(mimeMessageHelper.getMimeMessage());
//            }
//        } catch (MessagingException | IOException | TemplateException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public String geContentFromTemplate(UsuarioDTO usuarioDTO, TipoEmails tipoEmails) throws IOException, TemplateException {
//        Map<String, Object> dados = new HashMap<>();
//        dados.put("nome", usuarioDTO.getEmail());
//        dados.put("email", from);
//        if (tipoEmails.equals(TipoEmails.CREATE)) {
//            dados.put("texto1", "Estamos felizes em ter você em nosso sistema!");
//            dados.put("texto2", "Seu cadastro foi realizado com sucesso!");
//        } else if (tipoEmails.equals(TipoEmails.UPDATE)) {
//            dados.put("texto1", "Você atualizou seus dados com sucesso! ");
//            dados.put("texto2", "--------------------------------------");
//        } else if (tipoEmails.equals(TipoEmails.DELETE)) {
//            dados.put("texto1", "Que pena! Você perdeu o acesso ao nosso sistema!!");
//            dados.put("texto2", "--------------------------------------");
//        } else if (tipoEmails.equals(TipoEmails.ING_COMPRADO)) {
//            dados.put("texto1", "A compra do seu ingresso foi realizada com sucesso!");
//            dados.put("texto2", "Obrigado!");
//        }
//        Template template = fmConfiguration.getTemplate("email-template.html");
//        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
//        return html;
//    }
//
//    public String geContentFromTemplateToken(UsuarioDTO usuarioDTO, TipoEmails tipoEmails, String token)
//            throws IOException, TemplateException {
//        Map<String, Object> dados = new HashMap<>();
//        dados.put("nome", usuarioDTO.getEmail());
//        dados.put("email", from);
//        if (tipoEmails.equals(TipoEmails.REC_SENHA)) {
//            dados.put("texto1", "Solicitação de recuperação de senha feita com sucesso!" );
//            dados.put("texto2", "Seu token de recuperação é: " + token);
//        }
//        Template template = fmConfiguration.getTemplate("email-template.html");
//        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
//        return html;
//    }
}