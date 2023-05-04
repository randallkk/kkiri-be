package com.lets.kkiri.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import com.lets.kkiri.dto.fcm.FcmMessageDto;
import com.lets.kkiri.dto.noti.NotiLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final ObjectMapper objectMapper;
    @Value("${firebase.config.path}")
    String firebaseConfigPath;

    @Value("${firebase.scope}")
    String firebaseScope;

    @Value("${firebase.send.url}")
    String FCM_SEND_URL;
    public List<NotiLogDto> sendMessage(List<String> tokenList, String title, String body) throws IOException {
        List<NotiLogDto> results = new ArrayList<>();
        MulticastMessage message = MulticastMessage.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setChannelId("hurry")
                                .build())
                        .build())
                .addAllTokens(tokenList)
                .build();

        BatchResponse sendRes = null;
        try {
            sendRes = FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new IllegalArgumentException("FIREBASE ERROR");
        }

        List<SendResponse> responses = sendRes.getResponses();

        for (int i = 0; i < responses.size(); i++) {
            if(!responses.get(i).isSuccessful()) continue;
            String messageId = responses.get(i).getMessageId().split("/")[3];
            String token = tokenList.get(i);

            results.add(
                    NotiLogDto.builder()
                                        .messageId(messageId)
                                        .token(token)
                                        .title(title)
                                        .body(body)
                                        .image(null)
                                        .build()
            );
        }
        return results;
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessageDto fcmMessage = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(firebaseScope));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
