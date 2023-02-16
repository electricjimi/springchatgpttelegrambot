package io.fc.spring2bot;

import io.fc.spring2bot.bot.MyBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Spring2botApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(Spring2botApplication.class, args);

		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		try
		{
			telegramBotsApi.registerBot(new MyBot());
		}
		catch (Exception e){e.printStackTrace();}
	}

}
