package handlers;

import bot.Bot;
import command.CSVParser;
import command.Command;
import command.ParsedCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;
import java.util.*;

public class GameHandler extends AbstractHandler{
    private final String END_LINE = "\n";
    public HashMap<String,String> dataMap;
    private final CSVParser parser;
    String operationResultKey;

    public GameHandler(Bot bot) throws IOException {
        super(bot);
        parser = new CSVParser();
        parser.parseCSVIntoMap("/home/nik/IdeaProjects/TelegramBot/src/main/resources/dataset3.csv");
        operationResultKey = quizRandomizer(parser.getDataMap());

        dataMap = parser.getDataMap();
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) throws IOException {


        if (parsedCommand.getCommand().equals(Command.START_GAME)) {
            //operationResultKey = quizRandomizer(parser.getDataMap());
            bot.sendQueue.add(getMessageStartGame(chatId));
            bot.sendQueue.add(quizGenerator(chatId, operationResultKey));
            return "";
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        System.out.println(dataMap.containsKey(operationResultKey));
        System.out.println("Верный ответ: "+dataMap.get(operationResultKey)+" - это значение по ключу ");

        String str = dataMap.get(operationResultKey);
        str = str.trim().toUpperCase();

        String parsedCommandText=parsedCommand.getText();
        parsedCommandText = parsedCommandText.trim().toUpperCase();

        if (parsedCommandText.equals(str)){

            message.setText("Правильный ответ!");

            bot.sendQueue.add(message);
            operationResultKey = quizRandomizer(parser.getDataMap());
            bot.sendQueue.add(quizGenerator(chatId, operationResultKey));

            return "";
        } else {
            message.setText("Неверный ответ, давай попробуем другой вопрос");
            bot.sendQueue.add(message);
            operationResultKey = quizRandomizer(parser.getDataMap());
            bot.sendQueue.add(quizGenerator(chatId, operationResultKey));
            return "";
        }
    }

    //Возвращает рандомный ключ - страну
    private String quizRandomizer(HashMap<String ,String > map){
        Random generator = new Random();
        List<String> keys = new ArrayList<String>(map.keySet());
        String randomKey = keys.get(generator.nextInt(keys.size()));
        //String value = map.get(randomKey);
        return randomKey;
    }

    private SendMessage quizGenerator(String chatID,String countryName){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder question = new StringBuilder();
        question.append("Какой город является столицей страны "+countryName.replace(']',' ')+"?").append(END_LINE);

        sendMessage.setText(question.toString());
        return  sendMessage;
    }

    private SendMessage getMessageStartGame(String chatID){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Ты запустил игру в столицы").append(END_LINE);
        text.append("Это простая игра, как в города,").append(END_LINE);
        text.append("только я называю страну - а ты столицу этой страны").append(END_LINE);
        //text.append("Игру можно завершить в любой момент, написав [/stop_game](/stop_game)").append(END_LINE);
        //text.append("Для того, чтобы просмотреть сколько правильных столиц ты назвал введи \n [/score](/score)");

        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
