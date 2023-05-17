package com.lets.kkiri.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import com.lets.kkiri.dto.fcm.FcmMessageDto;
import com.lets.kkiri.dto.noti.NotiLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    @Value("${firebase.config.path}")
    String firebaseConfigPath;

    @Value("${firebase.scope}")
    String firebaseScope;

    @Value("${firebase.send.url}")
    String FCM_SEND_URL;

    public List<NotiLogDto> sendMessageToToken(FcmMessageDto messageDto) throws IOException {
        List<NotiLogDto> results = new ArrayList<>();
        MulticastMessage message = makeMessage(messageDto);

        List<String> tokenList = messageDto.getTokenList();

        BatchResponse sendRes = null;
        try {
            sendRes = FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new IllegalArgumentException("FIREBASE ERROR");
        }

        List<SendResponse> responses = sendRes.getResponses();

        for (int i = 0; i < responses.size(); i++) {
            if (!responses.get(i).isSuccessful()) continue;
            String messageId = responses.get(i).getMessageId().split("/")[3];
            String token = tokenList.get(i);

            NotiLogDto.NotiLogDtoBuilder notiLogDtoBuilder = NotiLogDto.builder()
                    .messageId(messageId)
                    .token(token)
                    .title(messageDto.getTitle())
                    .body(messageDto.getBody())
                    .image(null);

            results.add(notiLogDtoBuilder.build());
        }
        return results;
    }

    private MulticastMessage makeMessage(FcmMessageDto messageDto) throws JsonParseException, JsonProcessingException {
        Notification.Builder notificationBuilder = Notification.builder()
                .setTitle(messageDto.getTitle())
                .setBody(messageDto.getBody());

        AndroidConfig.Builder androidConfigBuilder = AndroidConfig.builder()
                .setTtl(3600 * 1000)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(
                        AndroidNotification.builder()
                                .setChannelId(messageDto.getChannelId())
                                .build()
                );

        MulticastMessage.Builder multicastMessageBuilder = MulticastMessage.builder()
                .setNotification(notificationBuilder.build())
                .setAndroidConfig(androidConfigBuilder.build())
                .putData("title", messageDto.getTitle())
                .addAllTokens(messageDto.getTokenList());

        if(messageDto.getPath() != null) multicastMessageBuilder.putData("path", messageDto.getPath().toString());
        if(messageDto.getMessage() != null) multicastMessageBuilder.putData("message", messageDto.getMessage());
        if(messageDto.getMoim() != null) {
            multicastMessageBuilder.putData("moimId", messageDto.getMoim().getId().toString());
            multicastMessageBuilder.putData("moimName", messageDto.getMoim().getName());
        }
        if(messageDto.getSender() != null) {
            multicastMessageBuilder.putData("senderNickname", messageDto.getSender().getNickname());
            multicastMessageBuilder.putData("kakaoId", messageDto.getSender().getKakaoId());
        }
        if(messageDto.getTime() != null) multicastMessageBuilder.putData("time", messageDto.getTime().toString());

        return multicastMessageBuilder.build();
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(firebaseScope));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
