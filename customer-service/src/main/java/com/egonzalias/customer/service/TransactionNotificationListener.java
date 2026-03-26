package com.egonzalias.customer.service;

import com.egonzalias.customer.dto.TransactionCompletedEvent;
import com.egonzalias.customer.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionNotificationListener {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionNotificationListener.class);

    private final CustomerRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionNotificationListener(
            CustomerRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${aws.sqs.queue.transaction-events}")
    public void handle(String payload) {
        try {
            log.debug("Received transaction event message");

            TransactionCompletedEvent event =
                    objectMapper.readValue(payload, TransactionCompletedEvent.class);

            repository.findById(event.customerId())
                    .ifPresentOrElse(customer -> {

                        String message = String.format(
                                "SMS to %s: %s of %.2f on account %s. New balance: %.2f",
                                customer.getPhone(),
                                event.type(),
                                event.amount(),
                                event.accountNumber(),
                                event.balanceAfter()
                        );

                        log.info(
                                "Sending transaction notification, customerId={}, accountNumber={}",
                                event.customerId(),
                                event.accountNumber()
                        );

                        log.debug("Notification content sent to customer: {}", message);

                    }, () -> {
                        log.warn(
                                "Transaction notification skipped, customer not found, customerId={}",
                                event.customerId()
                        );
                    });

        } catch (Exception e) {
            log.error("Failed to process transaction event message", e);
            throw new RuntimeException("Failed to process transaction event", e);
        }
    }
}

