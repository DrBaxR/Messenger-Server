package messenger.mail;

import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    public void sendMail(String to, String subject, String body) {
        Email fromEmail = new Email("parkingapplication3@gmail.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(fromEmail, subject, toEmail, content);
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
