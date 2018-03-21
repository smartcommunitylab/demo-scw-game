package it.smartcommunitylab.gamification.tnsmartweek.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@ConfigurationProperties(prefix = "game-engine")
public class GameEngineClient {


    private static Logger logger = LogManager.getLogger(GameEngineClient.class);

    private String url;
    private String gameId;
    private String climbPlayer;
    private String playAndGoPlayer;
    private String action;
    private String username;
    private String password;

    private ObjectMapper mapper = new ObjectMapper();

    public void gameClimbAction(double score) {
        sendAction(climbPlayer, score);
    }

    public void gamePlayAndGoAction(double score) {
        sendAction(playAndGoPlayer, score);
    }

    private void sendAction(String player, double score) {
        OkHttpClient client = new OkHttpClient();
        String basicHeader = Credentials.basic(username, password);
        String payload = payload(player, score);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), payload);
        Request request = new Request.Builder().url(url).addHeader("Authorization", basicHeader)
                .post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                logger.info("body request: {}", payload);
                logger.info("game-engine request SUCCESS");

            } else {
                logger.warn("game-engine request FAILED");
            }
        } catch (IOException e) {
            logger.error("Exception sending request to game-engine", e);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setGameId(String gameId) {
        this.gameId = gameId;
    }


    private String payload(String player, double score) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("gameId", gameId);
        payload.put("playerId", player);
        payload.put("actionId", action);
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("class-distance", score);
        inputData.put("meteo", "sun");
        inputData.put("school-date", new Date().getTime());
        inputData.put("participants", 1d);
        payload.put("data", inputData);
        mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            logger.error("Exception creating game-engine payload");
            return "";
        }


    }


    public void setClimbPlayer(String climbPlayer) {
        this.climbPlayer = climbPlayer;
    }


    public void setPlayAndGoPlayer(String playAndGoPlayer) {
        this.playAndGoPlayer = playAndGoPlayer;
    }


    public void setAction(String action) {
        this.action = action;
    }


    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

}
