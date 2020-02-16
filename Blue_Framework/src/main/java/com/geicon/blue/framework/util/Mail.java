package com.geicon.blue.framework.util;

import br.com.caelum.vraptor.environment.Environment;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Envio de e-mails
 *
 * @author Gabriel Duarte
 */
@ApplicationScoped
public class Mail {
    /**
     * Ambiente
     */
    @Inject
    private Environment environment;

    /**
     * Construtor
     */
    public Mail() {
    }

    /**
     * Verifica se o envio de e-mails está configurado
     *
     * @return true se sim / false se não
     */
    public boolean isAvailable() {
        String hostName;
        String user;
        String passwd;

        hostName = environment.get("mail.hostName");
        user = environment.get("mail.user");
        passwd = environment.get("mail.passwd");

        return hostName != null && !hostName.isEmpty()
                && user != null && !user.isEmpty()
                && passwd != null;
    }

    /**
     * Envio de e-mails
     *
     * @param to e-mails dos destinatários separados por ponto-e-vírgula (;)
     * @param cc e-mails das destinatários das cópias separados por
     * ponto-e-vírgula (;)
     * @param cco e-mails dos destinatários das cópias ocultas separados por
     * ponto-e-vírgula (;)
     * @param subject assunto do e-mail
     * @param content conte�do (HTML) do e-mail
     * @return true se o envio foi realizado com sucesso, false se não.
     * @throws EmailException
     * @throws java.io.UnsupportedEncodingException
     */
    public boolean send(String to, String cc, String cco, String subject, String content) throws EmailException, UnsupportedEncodingException {
        return send(to, cc, cco, subject, content, true, true);
    }

    /**
     * Envio de e-mails
     *
     * @param to e-mails dos destinatários separados por ponto-e-vírgula (;)
     * @param cc e-mails das destinatários das cópias separados por
     * ponto-e-vírgula (;)
     * @param cco e-mails dos destinatários das cópias ocultas separados por
     * ponto-e-vírgula (;)
     * @param subject assunto do e-mail
     * @param content conte�do (HTML) do e-mail
     * @param useSSL utilizar SSL
     * @param useTLS utilizar TLS
     * @return true se o envio foi realizado com sucesso, false se não.
     * @throws EmailException
     * @throws java.io.UnsupportedEncodingException
     */
    public boolean send(String to, String cc, String cco, String subject, String content, boolean useSSL, boolean useTLS) throws EmailException, UnsupportedEncodingException {
        boolean sendOk = false;

        if (to == null || subject == null) {
            return false;
        }

        String from = environment.get("mail.from");
        String fromMail = environment.get("mail.noreply");
        String htmlErrorMsg = environment.get("mail.htmlError");
        String hostName = environment.get("mail.hostName");
        String port = environment.get("mail.port");
        String user = environment.get("mail.user");
        String passwd = environment.get("mail.passwd");
        String noreply = environment.get("mail.noreply");
        String debug = environment.get("mail.debug");

        if (hostName != null && user != null && passwd != null) {
            HtmlEmail email = new HtmlEmail();

            ResourceBundle rb = ResourceBundle.getBundle("mail");
            String htmlContent = rb.getString("emailtemplate");

            email.setContent(htmlContent.replace("###TEXTO###", content), "text/html");

            email.setTextMsg(htmlErrorMsg);
            email.setHostName(hostName);

            if (debug == null || !debug.equals("true")) {
                String[] lstTo = to.split(";");
                String[] lstCc = cc != null ? cc.split(";") : new String[0];
                String[] lstCco = cco != null ? cco.split(";") : new String[0];

                if (lstTo.length == 0 && cc != null && !cc.isEmpty()) {
                    lstTo = cc.split(";");
                }

                for (String destinatario : lstTo) {
                    email.addTo(destinatario);
                }

                for (String destinatarioCc : lstCc) {
                    email.addCc(destinatarioCc);
                }

                for (String destinatarioCco : lstCco) {
                    email.addBcc(destinatarioCco);
                }
            }
            else {
                if (noreply != null) {
                    email.addTo(noreply);
                }
                else {
                    throw new EmailException("No reply mail is set. Debug cannot be done.");
                }
            }

            email.setFrom(fromMail, from);

            Set<InternetAddress> emailReply = new LinkedHashSet<>(0);
            emailReply.add(new InternetAddress(fromMail, from));

            email.setReplyTo(emailReply);
            email.setSubject(subject);
            email.setMsg(content);
            email.setCharset("UTF-8");
            email.setAuthentication(user, passwd);
            try {
                email.setSmtpPort(Integer.parseInt(port));
            }
            catch (NumberFormatException ex) {
                email.setSmtpPort(587);
            }
            //email.setSSL(false);
            email.setSSLOnConnect(useSSL);
            //email.setTLS(useTLS);
            email.setStartTLSEnabled(useTLS);

            return email.send() != null;
        }

        return sendOk;
    }
}
