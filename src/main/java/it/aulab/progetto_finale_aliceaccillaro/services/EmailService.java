package it.aulab.progetto_finale_aliceaccillaro.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}
