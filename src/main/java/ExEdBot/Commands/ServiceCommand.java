package ExEdBot.Commands;

import ExEdBot.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ServiceCommand implements IBotCommand {
    static final  Logger logger = LogManager.getLogger();
    private final String identifier;
    private final String description;

    public ServiceCommand(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    @Override public String getCommandIdentifier() {
        return identifier;
    }

    @Override public String getDescription() {
        return description;
    }

    @Override public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String userName = Utils.getUserName(message);

        logger.debug(
                String.format("Пользователь %s. Начато выполнение команды %s", userName, this.getCommandIdentifier()));

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), arguments[0]);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Команда %s. Пользователь: %s", e.getMessage(),
                    this.getCommandIdentifier(), userName));
            e.printStackTrace();
        }

        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
