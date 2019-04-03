package com.ericsson.example.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.mqtt")
public class MqttProperties {
    private Inbound inbound = new Inbound();
    private Outbound outbound = new Outbound();

    @Data
    public static class Inbound {
        private String urls;
        private String username;
        private String password;
        private String clientId;
        private String topic;
    }

    @Data
    public static class Outbound {
        private String urls;
        private String username;
        private String password;
        private String clientId;
        private String topic;
    }
}
