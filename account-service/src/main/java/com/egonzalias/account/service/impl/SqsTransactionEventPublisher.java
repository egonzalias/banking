package com.egonzalias.account.service.impl;

import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.egonzalias.account.service.TransactionEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsTransactionEventPublisher implements TransactionEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionEventPublisher.class);

    private final SqsTemplate sqsTemplate;
    private final String queueName;
    private final ObjectMapper objectMapper;

    public SqsTransactionEventPublisher(SqsTemplate sqsTemplate,
                                        @Value("${aws.sqs.queue.transaction-events}") String queueName,
                                        ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(TransactionCompletedEvent event) {
        try {
            log.debug(
                    "Publishing transaction event, accountNumber={}, type={}, amount={}",
                    event.accountNumber(),
                    event.type(),
                    event.amount()
            );

            String payload = objectMapper.writeValueAsString(event);
            sqsTemplate.send(queueName, payload);

            log.info(
                    "Transaction event published successfully, accountNumber={}",
                    event.accountNumber()
            );

        } catch (Exception e) {
            log.error(
                    "Failed to publish transaction event, accountNumber={}",
                    event.accountNumber(),
                    e
            );
            throw new RuntimeException("Failed to publish transaction event", e);
        }
    }
}
