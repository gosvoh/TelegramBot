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
    public HashMap<Long,String> answersUsers;

    public GameHandler(Bot bot) throws IOException {
        super(bot);
        parser = new CSVParser();
        parser.parseCSVIntoMap("/home/nik/IdeaProjects/TelegramBot/src/main/resources/1-Lvl-eu.csv");
        answersUsers = new HashMap<>();
        dataMap = parser.getDataMap();
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) throws IOException {


        if (parsedCommand.getCommand().equals(Command.START_GAME)) {
            operationResultKey = quizRandomizer(parser.getDataMap());
            answersUsers.put(update.getMessage().getChatId(),operationResultKey);
            bot.sendQueue.add(getMessageStartGame(update.getMessage().getChatId().toString()));
            bot.sendQueue.add(quizGenerator(update.getMessage().getChatId(), answersUsers.get(update.getMessage().getChatId())));
            return "";
        }

        if(answersUsers.get(update.getMessage().getChatId())==null){
            return "";
        }
        String str = dataMap.get(answersUsers.get(update.getMessage().getChatId()));
        str = str.trim().toUpperCase();

        String parsedCommandText = parsedCommand.getText();
        parsedCommandText = parsedCommandText.trim().toUpperCase();

        if (parsedCommandText.equals(str)){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.enableMarkdown(true);
            StringBuilder answerCorrect = new StringBuilder();
            answerCorrect.append("Правильный ответ!");

            sendMessage.setText(answerCorrect.toString());
            bot.sendQueue.add(sendMessage);

            operationResultKey = quizRandomizer(parser.getDataMap());
            answersUsers.put(update.getMessage().getChatId(), operationResultKey);
            bot.sendQueue.add(quizGenerator(update.getMessage().getChatId(), answersUsers.get(update.getMessage().getChatId())));

            return "";
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.enableMarkdown(true);
            StringBuilder answerBad = new StringBuilder();

            String tmp = str.toLowerCase();
            String correctAnswer = tmp.substring(0,1).toUpperCase()+tmp.substring(1);

            answerBad.append("Неверный ответ."+" Столица страны "+ answersUsers.get(update.getMessage().getChatId()) + " является " +correctAnswer+". Давай попробуем другой вопрос");

            sendMessage.setText(answerBad.toString());
            bot.sendQueue.add(sendMessage);

            operationResultKey = quizRandomizer(parser.getDataMap());
            answersUsers.put(update.getMessage().getChatId(),operationResultKey);
            bot.sendQueue.add(quizGenerator(update.getMessage().getChatId(), answersUsers.get(update.getMessage().getChatId())));

            return "";
        }
    }

    //Возвращает рандомный ключ - страну
    private String quizRandomizer(HashMap<String ,String > map){
        Random generator = new Random();
        List<String> keys = new ArrayList<String>(map.keySet());
        String randomKey = keys.get(generator.nextInt(keys.size()));
        return randomKey;
    }

    private SendMessage quizGenerator(Long chatID,String countryName){
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
        text.append("Игру можно завершить в любой момент, написав [/stop](/stop)").append(END_LINE);
        //text.append("Для того, чтобы просмотреть сколько правильных столиц ты назвал введи \n [/score](/score)");

        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
