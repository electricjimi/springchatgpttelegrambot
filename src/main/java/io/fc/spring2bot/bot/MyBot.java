package io.fc.spring2bot.bot;

import io.fc.spring2bot.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyBot extends TelegramLongPollingBot {

    private static ChatGPTService service;
    private static Environment env;

    @Override
    /**
     * Action invoked when the user sends a message to the chat.
     */
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        String text = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());

        pong(sendMessage, chatId, text);

        askGpt(sendMessage, chatId, text);
    }

    /**
     * Sends your request to chatGPT using #ChatGPTService.askChatGPT
     * @param sendMessage
     * @param chatId
     * @param text
     */
    private void askGpt(SendMessage sendMessage, String chatId, String text) {

        String gptResponse = service.askChatGPTText(text);
        try {
            sendMessage.setChatId(chatId);
            sendMessage.setText("GPT answers: " + gptResponse);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gives a feedback about what you are requesting to chatGPT
     * @param sendMessage
     * @param chatId
     * @param text
     */
    private void pong(SendMessage sendMessage, String chatId, String text) {
        sendMessage.setText("Hello! I'm sending this message to chatGPT: " + text);
        try {
            sendMessage.setChatId(chatId);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return env.getProperty("bot.name");
    }

    @Override
    public String getBotToken() {
        return env.getProperty("bot.token");
    }

    @Autowired
    public void setService(ChatGPTService service) {
        this.service = service;
    }
    @Autowired
    public void setEnv(Environment env) {
        MyBot.env = env;
    }
}
