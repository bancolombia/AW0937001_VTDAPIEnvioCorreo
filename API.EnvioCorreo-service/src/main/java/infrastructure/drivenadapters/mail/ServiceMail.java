package infrastructure.drivenadapters.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import infrastructure.drivenadapters.technicalogs.LOGGER;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ServiceMail {


    public SendGrid createService() {
        return new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    public boolean send(Mail mail, String strFrom, String strSubject, String strTo, Content content) {
        return send(createService(), mail, strFrom, strSubject, strTo, content);
    }

    public boolean send(SendGrid sg, Mail mail, String strFrom, String strSubject, String strTo, Content content) {

        LOGGER.info(this.getClass(), "Se inicia envio de correo con los parametros indicados");

        Email from = new Email(strFrom);
        String subject = strSubject;
        Email to = new Email(strTo);

        mail.setFrom(from);
        mail.setSubject(subject);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);
        mail.addContent(content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            LOGGER.debug(this.getClass(),
                    String.format("Status Code: %s - Headers: %s - Body: %s - ", response.getStatusCode(), response.getHeaders(), response.getBody()));
            LOGGER.info(this.getClass(), "Status Code: " + response.getStatusCode());

            if (response.getStatusCode() == javax.ws.rs.core.Response.Status.ACCEPTED.getStatusCode()) {
                return true;
            } else {
                return false;
            }

        } catch (IOException ex) {
            LOGGER.error(this.getClass(), ex.getMessage(), ex);
            return false;
        }

    }

    public void addAttachment(Mail mail, String fileName, InputStream is) {
        mail.addAttachments(new Attachments.Builder(fileName, is).build());
    }

    public void addAttachment(Mail mail, String pathImage, String strImage, String strImageId, String contentType) throws IOException {

        LOGGER.debug(this.getClass(), String.format("Attachment image:%s - cid:%s", strImage, strImageId));
        String filePath = pathImage.concat(strImage);

        Attachments attachment = new Attachments();
        attachment.setContent(convertFileToBase64(filePath));
        attachment.setFilename(strImage);
        attachment.setType(contentType);
        attachment.setContentId(strImageId);
        attachment.setDisposition("inline");
        mail.addAttachments(attachment);
    }

    private String convertFileToBase64(String file) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file)));
    }

}
