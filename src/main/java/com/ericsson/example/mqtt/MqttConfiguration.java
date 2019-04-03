package com.ericsson.example.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

//@Configuration
public class MqttConfiguration {
    @Autowired
    private MqttProperties mqttProperties;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageProducer inbound() {
        String[] inboundTopics = mqttProperties.getInbound().getTopic().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInbound().getUrls(), mqttProperties.getInbound().getClientId(),
                        inboundTopics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //log.info((String) message.getPayload());
            }

        };
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        String[] serverURIs = mqttProperties.getOutbound().getUrls().split(",");
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setServerURIs("tcp://192.168.10.100:1883", "tcp://host2:1883");
        factory.setServerURIs(serverURIs);
        factory.setCleanSession(false);
//        factory.setUserName("username");
//        factory.setPassword("password");
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttProperties.getOutbound().getTopic());
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}
