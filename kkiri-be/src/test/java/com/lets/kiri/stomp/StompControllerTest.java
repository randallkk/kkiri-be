package com.lets.kiri.stomp;

import com.lets.kkiri.dto.gps.GpsDto;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class StompControllerTest extends StompSupport {

    @Test
    @Description("gps/location/{roomId}로 gpsDto를 전송하면 /gps/{roomId}로 gpsDto가 전송되는지 확인")
    public void findLocation() throws ExecutionException, InterruptedException, TimeoutException {
        /* GIVEN */
        MessageFrameHandler<GpsDto> handler = new MessageFrameHandler<>(GpsDto.class);
        this.stompSession.subscribe("/gps/1", handler);

        /* WHEN */
        GpsDto gpsDto = GpsDto.builder().latitude(1.0).longitude(2.0).build();
        this.stompSession.send("/gps/location/1", gpsDto);

        /* THEN */
        GpsDto location = handler.getCompletableFuture().get(3, TimeUnit.SECONDS);

        assertThat(location, notNullValue());
        assertThat(location.getLatitude(), is(1.0));
        assertThat(location.getLongitude(), is(2.0));
    }

}