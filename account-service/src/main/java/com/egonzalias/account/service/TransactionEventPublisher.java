package com.egonzalias.account.service;

import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventPublisher {

    private final SqsTemplate sqsTemplate;
    private final String queueName;
    private final ObjectMapper objectMapper;

    public TransactionEventPublisher(SqsTemplate sqsTemplate, @Value("${aws.sqs.queue.transaction-events}") String queueName, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
        this.objectMapper = objectMapper;
    }

    public void publish(TransactionCompletedEvent event) {
        try{
            String payload = objectMapper.writeValueAsString(event);
            sqsTemplate.send(queueName, payload);

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish transaction event", e);
        }
    }
}

