import bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import service.MessageReceiver;
import service.MessageSender;

public class App {
    private static final Logger log = Logger.getLogger(App.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;
    private static final String BOT_ADMIN = "321644283";

    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();
        Bot testBot = new Bot("BotWorldWide","5279100609:AAHVr20ji5W13c07rPHZG3s25IKdPQxLrtA");

        MessageReceiver messageReceiver = new MessageReceiver(testBot);
        MessageSender messageSender = new MessageSender(testBot);

        testBot.botConnect();

        Thread receiver = new Thread(messageReceiver); //Работа до конца основного потока
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

    }
}
