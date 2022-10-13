package com.trepudox;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class Main {

    private static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("producer");
        factory.setPassword("producer");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             Scanner scanner = new Scanner(System.in)) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            System.out.println("If you want to leave the program just type 'exit()'");
            String message;
            do {
                System.out.print("Type your message: ");
                message = scanner.nextLine();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            } while(!message.equals("exit()"));

            log.info("Message sent");
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}