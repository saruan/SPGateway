package com.kbds.gateway.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.kbds.gateway.dto.ServiceLogDTO;
import lombok.Data;

/**
 *
 * <pre>
 *  Class Name     : KafkaConfiguration.java
 *  Description    : Kafka 추가 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-22    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring")
@Data
public class KafkaConfiguration {

  private Map<String, Map<String, String>> kafka;

  private final String BOOTSTRAP_SERVERS = "bootstrap-servers";

  @Bean
  public ProducerFactory<String, ServiceLogDTO> producerFactory() {

    Map<String, Object> configProps = new HashMap<>();

    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        getKafka().get(BOOTSTRAP_SERVERS).get("0"));
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, ServiceLogDTO> kafkaTemplate() {

    return new KafkaTemplate<>(producerFactory());
  }
}
