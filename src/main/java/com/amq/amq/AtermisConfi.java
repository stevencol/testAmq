package com.amq.amq;

import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl;
import org.springframework.context.annotation.Bean;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@org.springframework.context.annotation.Configuration
public class AtermisConfi {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ActiveMQServer activeMQServer() throws Exception {
        /// Confi  para el broker
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false)
                .setSecurityEnabled(true)
                .setJournalDirectory("target/data/journal")
                .setBindingsDirectory("target/data/bindings")
                .setGlobalMaxSize(128L * 1024L * 1024L)
                .addAcceptorConfiguration("tcp", "tcp://localhost:61616");


        //Colas

        QueueConfiguration qc = new QueueConfiguration();
        qc.setAddress("cola-audit-address");
        qc.setName("cola-audit");
        qc.setRoutingType(RoutingType.ANYCAST);
        qc.setDurable(false);

        configuration.addQueueConfiguration(qc);

        SimpleSecurityManager sp = new SimpleSecurityManager("dev", "devPass");

        return new ActiveMQServerImpl(configuration, sp);
    }
}
