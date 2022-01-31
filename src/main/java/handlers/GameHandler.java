package handlers;

import bot.Bot;
import command.CSVParser;
import command.ParsedCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameHandler extends AbstractHandler{
    private final String END_LINE = "\n";
    private HashMap<String,String> capitalsAndCountries;

    public GameHandler(Bot bot) {
        super(bot);

    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) throws IOException {
        bot.sendQueue.add(getMessageStartGame(chatId));
        CSVParser parser = new CSVParser();
        parser.parseCSVIntoMap("/home/nik/IdeaProjects/TelegramBot/src/main/resources/dataset3.csv");
        String operationResult = quizRandomizer(parser.getCapitalsOfTheWorldRus());
        bot.sendQueue.add(quizGenerator(chatId, operationResult));

        return "";
    }



    private String quizRandomizer(HashMap<String ,String > map){
        Random generator = new Random();
        List<String> keys = new ArrayList<String>(map.keySet());
        String randomKey = keys.get(generator.nextInt(keys.size()));
        String value = map.get(randomKey);
        return value;
    }

    private SendMessage quizGenerator(String chatID,String countryName){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder question = new StringBuilder();
        question.append("Какой город является столицей страны"+countryName.replace(']',' ')+"?").append(END_LINE);

        sendMessage.setText(question.toString());
        return  sendMessage;
    }

    private SendMessage getMessageStartGame(String chatID){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Вы запустили игру в столицы").append(END_LINE);
        text.append("Это простая игра как в города").append(END_LINE);
        text.append("только я называю страну - а ты столицу этой страны").append(END_LINE);
        text.append("Игру можно завершить в любой момент написав [/stop_game](/stop_game)").append(END_LINE);
        text.append("Для того чтобы просмотреть сколько правильных столиц ты назвал введи \n [/score](/score)");

        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
