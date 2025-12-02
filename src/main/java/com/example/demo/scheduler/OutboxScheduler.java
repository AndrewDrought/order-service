package com.example.demo.scheduler;

import com.example.demo.entity.Outbox;
import com.example.demo.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void processOutbox() {
        List<Outbox> pendingEvents = outboxRepository.findByStatus("PENDING");
        for (Outbox event : pendingEvents) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getPayload());
                event.setStatus("SENT");
                outboxRepository.save(event);
                System.out.println("Sent event to Kafka: " + event.getId());
            } catch (Exception e) {
                System.err.println("Failed to send event: " + event.getId());
                // Ideally, implement retry logic or exponential backoff here
            }
        }
    }
}
