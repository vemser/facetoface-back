package br.com.vemser.facetoface.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private freemarker.template.Configuration fmConfiguration;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private MimeMessage mimeMessage;
    private String from = "fromteste@email.com.br";

    @Before
    public void init() {
        ReflectionTestUtils.setField(emailService, "from", from);
    }

    @Test
    public void deveEnviarEmailComAConfirmacaoDaEntrevista() throws IOException, MessagingException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String email = "teste@email.com.br";
        final String token = "$123token";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(any())).thenReturn(template);

        emailService.sendEmailConfirmacaoEntrevista(email, token);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveEnviarEmailComASenha() throws IOException, MessagingException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String email = "teste@email.com.br";
        final String senha = "123";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(any())).thenReturn(template);

        emailService.sendEmailEnvioSenha(email, senha);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveEnviarEmailDeRecuperacaoDeSenha() throws IOException, MessagingException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String email = "teste@email.com.br";
        final String token = "$123token";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(any())).thenReturn(template);

        emailService.sendEmailRecuperacaoSenha(email, token);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveEnviarEmail() throws IOException, MessagingException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String email = "teste@email.com.br";
        final String token = "$123token";
        final String nomeTemplate = "template.html";
        final String assunto = "Recuperação de senha concluída com sucesso.";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(nomeTemplate)).thenReturn(template);

        emailService.sendEmail(email, token, nomeTemplate, assunto);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveRetornarUmaExcecaoQuandoOcorrerUmErroNoEnvioDoEmail() throws IOException, MessagingException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String email = "teste@email.com.br";
        final String token = "$123token";
        final String nomeTemplate = "template.html";
        final String assunto = "Recuperação de senha concluída com sucesso.";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(nomeTemplate)).thenReturn(template);

        emailService.sendEmail(email, token, nomeTemplate, assunto);
    }

    @Test
    public void deveTestarGetContentFromTemplate() throws IOException, TemplateException {
        Template template = new Template("template.html", Reader.nullReader());

        final String token = "$123token";
        final String nomeTemplate = "template.html";

        when(fmConfiguration.getTemplate(nomeTemplate)).thenReturn(template);

        String content = emailService.getContentFromTemplate(token, nomeTemplate);

        assertNotNull(content);
    }
}
