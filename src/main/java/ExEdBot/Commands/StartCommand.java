package ExEdBot.Commands;

import ExEdBot.KeyboardBuilder;
import ExEdBot.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand extends ServiceCommand {
    public StartCommand() {
        super(Commands.START.getCommandIdentifier(), Commands.START.getCommandExtendedDescription());
    }

    @Override public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String userName = Utils.getUserName(message);

        logger.debug(
                String.format("Пользователь %s. Начато выполнение команды %s", userName, this.getCommandIdentifier()));

        SendMessage sendMessage =
                new SendMessage(message.getChatId().toString(), Commands.START.getCommandExtendedDescription());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(new KeyboardBuilder(Commands.HELP, Commands.AUTHENTICATION, Commands.UPLOAD));
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