package com.lets.kkiri.controller;

import com.lets.kkiri.dto.gps.GpsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Controller
public class GpsController {

    @Qualifier("gpsRabbitTemplate")
    private final RabbitTemplate template;

    private final static String GPS_EXCHANGE_NAME = "gps.exchange";
    private final static String GPS_QUEUE_NAME = "gps.queue";

//    @MessageMapping("gps.location.{roomId}")
//    public void send(GpsDto gpsDto, @DestinationVariable String roomId){
//        gpsDto.setRegDate(LocalDateTime.now());
//        template.convertAndSend(GPS_EXCHANGE_NAME, "room." + roomId, gpsDto); // exchange
//    }

    @MessageMapping("/gps")
    public void send(){
        template.convertAndSend(GPS_EXCHANGE_NAME, "room.1", "gpsDto"); // exchange
    }

    @RabbitListener(queues = GPS_QUEUE_NAME)
    public void receive(GpsDto gpsDto){
        System.out.println("lng : " + gpsDto.getLongitude() + "lat : " + gpsDto.getLatitude());
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String name) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "Hello " + name + "!";
    }

}