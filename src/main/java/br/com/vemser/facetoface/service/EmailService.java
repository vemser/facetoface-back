package br.com.vemser.facetoface.service;

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
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

//    @Value("${link.confirmacao.entrevista}")
//    private final String link;

    public void sendEmailConfirmacaoEntrevista(String email, String token) {
        final String subject = "Confirmação de entrevista.";
        sendEmail(email, token, "envio-senha-template.html", subject);
    }

    public void sendEmailEnvioSenha(String email, String senha) {
        String subject = "Cadastro concluído com sucesso.";
        sendEmail(email, senha, "envio-senha-template.html", subject);
    }

    public void sendEmailRecuperacaoSenha(String email, String token) {
        final String subject = "Recuperação de senha concluída com sucesso.";
        sendEmail(email, token, "envio-senha-template.html", subject);
    }

    public void sendEmail(String email, String info, String nomeTemplate, String assunto) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplate(info, nomeTemplate), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplate(String info, String nomeTemplate) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", from);
        dados.put("texto1", info);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }
}
