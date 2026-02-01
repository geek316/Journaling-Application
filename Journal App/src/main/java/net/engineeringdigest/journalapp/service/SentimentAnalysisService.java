package net.engineeringdigest.journalapp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public String getSentiment(String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        return "";
    }
}
