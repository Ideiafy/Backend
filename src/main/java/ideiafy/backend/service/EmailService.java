package ideiafy.backend.service;

import ideiafy.backend.model.TwoFactorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendCode(String to, String code, TwoFactorType type) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);

        message.setSubject(getSubject(type));
        message.setText(getBody(code, type));

        sender.send(message);
    }

    private String getSubject(TwoFactorType type) {
        return switch (type) {
            case ACTIVATION -> "Ative sua conta no Ideiafy";
            case PASSWORD_RESET -> "Redefinição de senha - Ideiafy";
        };
    }

    private String getBody(String code, TwoFactorType type) {
        return switch (type) {

            case ACTIVATION -> """
                    Olá,

                    Seja bem-vindo(a) ao Ideiafy!

                    Para ativar sua conta, utilize o código abaixo:

                    ========================
                           %s
                    ========================

                    Este código expira em 5 minutos.

                    Caso você não tenha criado uma conta, ignore este e-mail.

                    Atenciosamente,
                    Equipe Ideiafy
                    """.formatted(code);

            case PASSWORD_RESET -> """
                    Olá,

                    Recebemos uma solicitação para redefinir sua senha.

                    Utilize o código abaixo para continuar:

                    ========================
                           %s
                    ========================

                    Este código expira em 5 minutos.

                    Se você não solicitou esta alteração, ignore este e-mail.

                    Atenciosamente,
                    Equipe Ideiafy
                    """.formatted(code);
        };
    }
}