package handlers;

import bot.Bot;
import command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

public class StopHandler extends AbstractHandler {
    private static final Logger log = Logger.getLogger(SystemHandler.class);
    private final String END_LINE = "\n";

    public StopHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) throws IOException {
        bot.sendQueue.add(getMessageStop(chatId));

        return "";
    }

    private SendMessage getMessageStop(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("До скорой встречи.").append(END_LINE);
        text.append("Надеюсь увидимся снова!").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

}
