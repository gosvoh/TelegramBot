package handlers;

import bot.Bot;
import command.ParsedCommand;
import command.Parser;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

public abstract class AbstractHandler {
    Bot bot;

    AbstractHandler(Bot bot){
        this.bot=bot;
    }

    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update) throws IOException;
}
