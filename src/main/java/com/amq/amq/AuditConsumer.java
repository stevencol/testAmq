package com.amq.amq;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.activemq.artemis.api.core.client.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("activeMQServer")
public class AuditConsumer {

    private ServerLocator locator;
    private ClientSessionFactory sessionFactory;
    private ClientSession session;
    private ClientConsumer consumer;

    @PostConstruct
    public void start() throws Exception {

        // Localizador CORE
        locator = ActiveMQClient.createServerLocator("tcp://localhost:61616");
        sessionFactory = locator.createSessionFactory();

        // Sesión con usuario y contraseña
        session = sessionFactory.createSession(
                "dev",
                "devPass",
                false,   // xa
                true,    // autoCommitSends
                true,    // autoCommitAcks
                false,   // preAcknowledge
                0
        );

        // Consumidor de la cola existente
        consumer = session.createConsumer("cola-audit");

        session.start();


        class  UserDto{
            String name;
            String lastName;
            public  UserDto(String name, String lastName){
                this.name = name;
                this.lastName = lastName;
            }
        }


        System.out.println("AuditConsumer escuchando en cola-audit...");

        // Listener permanente
        consumer.setMessageHandler(message -> {
            try {
                String body = message.getBodyBuffer().readString();

                for (int i = 0; i < 100; i++) {
                    System.out.println("Mensaje recibido: " + body +"Steven");

                }
                System.out.println("Mensaje recibido: " + body +"Steven");
                message.acknowledge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @PreDestroy
    public void stop() throws Exception {
        System.out.println("Cerrando AuditConsumer...");

        if (consumer != null) consumer.close();
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();
        if (locator != null) locator.close();
    }
}

