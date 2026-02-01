package net.engineeringdigest.journalapp.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.enums.Sentiment;
import net.engineeringdigest.journalapp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Scheduled(cron = "0 0 9 * * SUN")
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSAMail() {

        log.info("Scheduler started for SentimentAnalysis");

        List<User> users = userRepository.getUsersForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getSentiment)
                    .toList();
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiment 4 Last 7 Days", mostFrequentSentiment.toString());
            }

        }

    }
}
