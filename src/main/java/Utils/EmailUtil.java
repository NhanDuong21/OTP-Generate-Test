package Utils;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    // Bạn đổi 2 biến này theo email gửi đi (sender)
    // Với Gmail: dùng App Password, không dùng password đăng nhập thường
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    private static final String SENDER_EMAIL = "sgoku4880@gmail.com";
    private static final String SENDER_APP_PASSWORD = "illa ftat juta irhh";

    public static void sendOtpEmail(String toEmail, String otp, int ttlMinutes) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "LandHouse Management"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP code");

            String body = ""
                    + "OTP của bạn là: " + otp + "\n"
                    + "OTP có hiệu lực trong " + ttlMinutes + " phút.\n\n"
                    + "Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.";

            message.setText(body);

            Transport.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }
}
