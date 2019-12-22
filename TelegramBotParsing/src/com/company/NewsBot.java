package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class NewsBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String message = "";
        try {
            Document doc = Jsoup.connect("http://ekaraganda.kz/").get();
            Elements blck = doc.select(".blck");
            for (Element e : blck) {
                message += e.text() + "\n";
            }
            sendMessage(message, update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "helloTuringParserBot";
    }

    @Override
    public String getBotToken() {
        return "796691627:AAFrPL3Takp5psZzT2l4sWuZ_kd6DyFhM6g";
    }

    private void sendMessage(String message, Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        }
        catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}
