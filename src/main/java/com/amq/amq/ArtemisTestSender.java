package com.amq.amq;

import com.google.gson.Gson;
import org.apache.activemq.artemis.api.core.*;
import org.apache.activemq.artemis.api.core.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisTestSender {

    @Bean
    public CommandLineRunner enviarMensajePrueba() {
        return args -> {

            ServerLocator locator = ActiveMQClient.createServerLocator(
                    "tcp://localhost:61616"
            );

            ClientSessionFactory factory = locator.createSessionFactory();

            ClientSession session = factory.createSession(
                    "dev",
                    "devPass",
                    false,
                    true,
                    true,
                    false,
                    locator.getAckBatchSize()
            );

            session.start();

            String address = "cola-audit-address";
            String queue = "cola-audit";


            ClientProducer producer =
                    session.createProducer(address);

            ClientMessage message = session.createMessage(false);


            Gson gson = new Gson();



            String data = gson.toJson(new  User("santiago"));
            message.getBodyBuffer().writeString(data);
            producer.send(message);
            System.out.println("Mensaje enviado correctamente al broker");

            session.close();
            factory.close();
            locator.close();
        };
    }
}
class User{
    String name;
    public User(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return "User{name='" + name + "'}";
    }

}
