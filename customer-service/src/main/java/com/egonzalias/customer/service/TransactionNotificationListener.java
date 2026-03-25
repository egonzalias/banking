package com.egonzalias.customer.service;


import com.egonzalias.customer.dto.TransactionCompletedEvent;
import com.egonzalias.customer.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionNotificationListener {

    private final CustomerRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionNotificationListener(CustomerRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${aws.sqs.queue.transaction-events}")
    public void handle(String payload) {
        try{
            TransactionCompletedEvent event =
                    objectMapper.readValue(payload, TransactionCompletedEvent.class);

            repository.findById(event.customerId()).ifPresentOrElse(customer -> {

                String message = String.format(
                        "SMS to %s: %s of %.2f on account %s. New balance: %.2f",
                        customer.getPhone(),
                        event.type(),
                        event.amount(),
                        event.accountNumber(),
                        event.balanceAfter()
                );

                //log.info(message);
                System.out.println("Sending message text: "+message);
            },()->{
                System.out.println(
                        "SMS NOT sent. Customer with id " + event.customerId()
                                + " not found in customer-service database."
                );
            });
        } catch (Exception e) {
            //log.error("Failed to process transaction event", e);
            System.out.println("Failed to process transaction event:"+e.getMessage());
        }

    }

}
