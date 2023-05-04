package com.lets.kkiri.controller;

import com.lets.kkiri.dto.gps.GpsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @MessageMapping("gps.location.{roomId}")
    public void send(GpsDto gpsDto, @DestinationVariable String roomId){
        gpsDto.setRegDate(LocalDateTime.now());

        template.convertAndSend(GPS_EXCHANGE_NAME, "room." + roomId, gpsDto); // exchange
    }

    @RabbitListener(queues = GPS_QUEUE_NAME)
    public void receive(GpsDto gpsDto){
        System.out.println("lng : " + gpsDto.getLongitude() + "lat : " + gpsDto.getLatitude());
    }

}