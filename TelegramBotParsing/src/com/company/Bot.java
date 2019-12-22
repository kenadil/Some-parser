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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private List<String[]> urls = new ArrayList<>();
    private int choice = 0;
    @Override
    public void onUpdateReceived(Update update) {
        String input = update.getMessage().getText();
        if (input.equals("/start")) {
            sendMessage("Greetings, wanderer! \nI am Turing Bot! My aim is to set my guidance over smth u'd be interested in! \n Send me anything you want to find on WikiHow! \n (also I please to excuse me, because I'm not enough intelligent to send pictures from site as well as bring you comfy command buttons to ease your navigation :c)", update);
        }
        else if (urls.isEmpty()){
            try {
                urls = getUrls(input);
                String message = "Here is your result(s): ";
                for (int i = 0; i < 10; i++) {
                    message += "\n" +  (i + 1) + ") " + urls.get(i)[1];
                }
                sendMessage(message, update);
                sendMessage("Enter the digit of your result, or type 'quit' to clear search field!", update);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (!urls.isEmpty() && (input.toLowerCase().equals("quit"))) {
            urls.clear();
            sendMessage("Search field is cleared! Proceed to another search!", update);
        }
        else {
            sendMessage(urls.get(Integer.parseInt(input)-1)[1] + "\n" + urls.get(Integer.parseInt(input) - 1)[0] , update);
            parse(update, urls.get(Integer.parseInt(input)-1)[0]);
            urls.clear();
        }
    }

    private void parse(Update update, String input) {
        List<String> response = new ArrayList<>();
        int methods = 1;

        System.out.println("Parsing...");
        try {
            Document doc = Jsoup.connect(input).timeout(6000).get();

            //Elements htmlElements = doc.getElementsByAttributeValue("class", "mfe-reco");

            Elements htmlElements = doc.select("div.steps");

            for (Element step : htmlElements) {
                int st = 1;
                sendMessage("Method " + methods++, update);
                for (Element description : step.select(".section_text .step")) {
                    String desc = description.text();
                    sendMessage(st++ + ") " + desc, update);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Parsing is done!");

        System.out.println("Done!");
    }

    private List<String[]> getUrls(String command) throws IOException {
        command = command.replaceAll(" ", "+");
        Document dc = Jsoup.connect("https://ru.wikihow.com/wikiHowTo?search=" + command).get();
        List<String[]> urls = new ArrayList<>();

        Elements htmlElements = dc.select(".result_link");

        for (Element link : htmlElements) {
            String div = link.getElementsByClass("result_title").text();
            String[] temp = {link.attr("href"), div};
            urls.add(temp);
        }

        return urls;
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

    @Override
    public String getBotUsername() {
        return "helloTuringParserBot";
    }

    @Override
    public String getBotToken() {
        return "796691627:AAFrPL3Takp5psZzT2l4sWuZ_kd6DyFhM6g";
    }
}
