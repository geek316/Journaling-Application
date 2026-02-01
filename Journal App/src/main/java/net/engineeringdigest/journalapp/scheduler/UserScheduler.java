package net.engineeringdigest.journalapp.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalapp.service.EmailService;
import net.engineeringdigest.journalapp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN")
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSAMail() {

        log.info("Scheduler started for SentimentAnalysis");

        List<User> users = userRepository.getUsersForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> filteredEntries = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getContent)
                    .toList();
            String entry = String.join(", ", filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail(user.getEmail(), "Sentiment 4 Last 7 Days", sentiment);
        }

    }
}
