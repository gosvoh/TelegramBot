package handlers;

import bot.Bot;
import command.Command;
import command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class SystemHandler extends AbstractHandler {
    private static final Logger log = Logger.getLogger(SystemHandler.class);
    private final String END_LINE = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command){
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "Ваш telegramId: "+update.getMessage().getFrom().getId();

            case NONE:
            case GAME_OVER:
            case SAY_SCORE:
            case NOT_FOR_ME:
        }
        return "";
    }


    private SendMessage getMessageHelp(String chatID){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("*Помощь*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - выводит стартовое сообщение").append(END_LINE);
        text.append("[/help](/help) - показывает доступные команды").append(END_LINE);
        text.append("[/start_game](/start_game) - начинает игру в столицы").append(END_LINE);
        text.append("[/id](/id) - показывает ваш ID в telegram ").append(END_LINE);
        text.append("/*notify* _time-in-sec_  - заглушить бота на время (в секундах)").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private SendMessage getMessageStart(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Привет. Я  *").append(bot.getBotUsername()).append("*").append(END_LINE);
        text.append("Я создан Тереховым Никитой в качестве тестового задания специально для Just AI").append(END_LINE);
        text.append("Давай сыграем в игру - столицы?").append(END_LINE);
        text.append("Ты можешь увидеть все команды написав боту [/help](/help)");

        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
