package ExEdBot;

import ExEdBot.Commands.Commands;
import ExEdBot.Commands.HelpCommand;
import ExEdBot.Commands.ServiceCommand;
import ExEdBot.Commands.StartCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Properties;

public class Bot extends TelegramLongPollingCommandBot {
    private static final Logger logger = LogManager.getLogger();

    private final String name;
    private final String token;

    public Bot() {
        super();
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = properties.getProperty("BOT_NAME");
        token = properties.getProperty("BOT_TOKEN");

        register(new StartCommand());
        register(new HelpCommand());
        register(new ServiceCommand(Commands.AUTHENTICATION.getCommandIdentifier(), Commands.AUTHENTICATION.getCommandExtendedDescription()));
        register(new ServiceCommand(Commands.UPLOAD.getCommandIdentifier(), Commands.UPLOAD.getCommandExtendedDescription()));
        logger.info("ExEdBot.Bot started");
    }

    @Override public String getBotUsername() {
        return name;
    }

    private String getIdentifier(String name) {
        for (var o : Commands.values()) {
            if (o.getCommandBtnName().equals(name))
                return o.getCommandIdentifier();
        }
        return "";
    }

    @Override public void processNonCommandUpdate(Update update) {
        if (!update.hasMessage() || update.getMessage().getText().isEmpty()) return;

        IBotCommand command = this.getRegisteredCommand(getIdentifier(update.getMessage().getText()));
        if (command != null) {
            command.processMessage(this, update.getMessage(), new String[]{command.getDescription()});
            return;
        }

        SendMessage msg = new SendMessage();
        msg.setChatId(String.valueOf(update.getMessage().getChatId()));
        msg.setText("Command not found");
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        logger.info(update.getMessage().getText());
    }

    @Override public String getBotToken() {
        return token;
    }

    @Override public void onRegister() {
        logger.info("ExEdBot.Bot registered");
    }
}
