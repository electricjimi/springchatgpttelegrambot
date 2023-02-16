package io.fc.spring2bot.service;

import io.fc.spring2bot.constants.BotConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ChatGPTService {

    public static final String CHOICES = "choices";
    public static final String TEXT = "text";
    @Value("${bot.text.temperature}")
    Double temperature;
    @Value("${bot.text.model}")
    String textModel;
    @Value("${bot.text.top-p}")
    Double topP;
    @Value("${bot.text.frequency-penalty}")
    Double freqPenalty;
    @Value("${bot.text.presence-penalty}")
    Double presPenalty;
    @Value("${api.token}")
    String apiToken;
    @Value("${bot.text.max-tokens}")
    Integer maxTokens;
    @Value(("${api.url.completions}"))
    String urlCompletions;

    /**
     * Sends a request to chatGPT's text generator
     * @param msg
     * @return
     */
    public String askChatGPTText(String msg){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = setHeaders();

        JSONObject request = new JSONObject();
        request.put(BotConstants.MODEL, textModel);
        request.put(BotConstants.PROMPT, msg);
        request.put(BotConstants.TEMPERATURE, temperature);
        request.put(BotConstants.MAX_TOKENS, maxTokens);
        request.put(BotConstants.TOP_P, topP);
        request.put(BotConstants.FREQUENCY_PENALTY, freqPenalty);
        request.put(BotConstants.PRESENCE_PENALTY, presPenalty);

        HttpEntity<String> requestEntity = new HttpEntity<>(request.toString(), headers);

        URI chatGptUrl = URI.create(urlCompletions);
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(chatGptUrl, requestEntity, String.class);

        JSONObject responseJson = new JSONObject(responseEntity.getBody());
        JSONArray choices = (JSONArray) responseJson.get(CHOICES);

        JSONObject firstChoice = (JSONObject) choices.get(0);
        return (String) firstChoice.get(TEXT);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(BotConstants.AUTHORIZATION, apiToken);
        return headers;
    }
}
