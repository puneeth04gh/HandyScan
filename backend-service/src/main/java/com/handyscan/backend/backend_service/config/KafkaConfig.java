package com.handyscan.backend.backend_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class KafkaConfig {
    @Value(value = "${KAFKA_BOOTSTRAP_SERVERS}")
    private String bootstrapAddress;

    @Value(value = "${KAFKA_JAAS_CONFIG}")
    private String jaasConfig;

    @Value(value = "${KAFKA_SECURITY_PROTOCOL}")
    private String securityProtocol;

    @Value(value = "${KAFKA_SASL_MECHANISM}")
    private String saslMechanism;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        log.info("Initializing the kafka admin");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        configs.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        configs.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaProducer(){
        log.info("Initializing the kafka producer");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        configs.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        configs.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configs));
    }
}
