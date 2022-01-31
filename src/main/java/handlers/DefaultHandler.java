package handlers;

import bot.Bot;
import command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

public class DefaultHandler  extends AbstractHandler {
    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }

}
