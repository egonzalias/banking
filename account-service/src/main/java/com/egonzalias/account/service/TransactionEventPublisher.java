package com.egonzalias.account.service;

import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


public interface TransactionEventPublisher {
    void publish(TransactionCompletedEvent event);
}

