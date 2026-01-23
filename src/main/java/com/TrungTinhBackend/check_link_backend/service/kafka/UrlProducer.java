package com.TrungTinhBackend.check_link_backend.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlProducer {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendUrl(String url){
         kafkaTemplate.send("url-topic",url);
    }
}
