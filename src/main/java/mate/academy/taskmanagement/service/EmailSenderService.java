package mate.academy.taskmanagement.service;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);
}
