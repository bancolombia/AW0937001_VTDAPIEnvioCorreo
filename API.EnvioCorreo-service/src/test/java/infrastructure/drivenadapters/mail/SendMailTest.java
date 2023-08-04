package infrastructure.drivenadapters.mail;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import util.UtilTestSM;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendMailTest {

    private static final Logger logger = LogManager.getLogger(SendMailTest.class);

    SendGrid sendGrid;
    Response response;
    Mail mail;
    ServiceMail sendMail;
    String from;
    String subject;
    String to;
    Content content;


    @Before
    public void initialice() {
        UtilTestSM.setConfiguracion();
        sendGrid = mock(SendGrid.class);
        mail = mock(Mail.class);
        response = new Response();

        sendMail = new ServiceMail();
        from = "no-reply@bancolombia.com";
        subject = "Sending with SendGrid is Fun with params";
        to = "dddddd@gmail.com";
        content = new Content("text/plain", "and easy to do anywhere, even with Java");

    }


    @Test
    public void givenParamters_SendMailFail() {
        //Arrange

        //mock
        try {
            response.setStatusCode(javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode());
            when(sendGrid.api(any())).thenReturn(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Act
        boolean result = sendMail.send(sendGrid, mail, from, subject, to, content);
        //Assert
        assertFalse(result);
    }

    @Test
    public void givenParamters_SendMailSuccess() {
        //Arrange

        //mock
        try {
            response.setStatusCode(javax.ws.rs.core.Response.Status.ACCEPTED.getStatusCode());
            when(sendGrid.api(any())).thenReturn(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Act
        boolean result = sendMail.send(sendGrid, new Mail(), from, subject, to, content);

        //Assert
        assertTrue(result);
    }


    @Test
    public void givenDocument_checkAttachment() {
        //Arrange
        Mail mail = new Mail();

        //Act
        ServiceMail sendMail = new ServiceMail();
        sendMail.addAttachment(mail, "archivo.pdf", UtilTestSM.pdfis());

        //Assert
        assertFalse(mail.getAttachments().isEmpty());
    }

    @Test
    public void givenImagen_checkAttachment() {
        //Arrange
        Mail mail = new Mail();

        //Act
        ServiceMail sendMail = new ServiceMail();
        try {
            sendMail.addAttachment(mail, "src/test/resources/img/", "img.png", "CARD_IMAGE_1", "png");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //Assert
        assertFalse(mail.getAttachments().isEmpty());
    }

}
