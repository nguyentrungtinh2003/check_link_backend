package com.TrungTinhBackend.check_link_backend.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlConsumer {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "url-topic")
    public void consumer(String url){
        String result = "Url "+url;
        simpMessagingTemplate.convertAndSend("/topic/notifications/",result);
    }
}
