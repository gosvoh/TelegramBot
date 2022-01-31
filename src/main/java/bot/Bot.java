package bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final org.apache.log4j.Logger log = Logger.getLogger(Bot.class);
    private final int RECONNECT_PAUSE = 10000;

    public final Queue<Object> sendQueue = new ConcurrentLinkedDeque<>(); //Многопоточные очереди в обе стороны
    public final Queue<Object> receiveQueue = new ConcurrentLinkedDeque<>();

    @Setter
    @Getter
    private String botName;

    @Setter
    private String botToken;

    public Bot(String botName,String botToken){
        this.botName=botName;
        this.botToken=botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: "+update.getUpdateId());
        System.out.println(update.getMessage().getText()+" от "+update.getMessage().getFrom());
        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        log.debug("Bot name: " + botName);
        return botName;
    }

    @Override
    public String getBotToken() {
        log.debug("Bot token: "+ botToken);
        return botToken;
    }

    public void botConnect(){
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            telegramBotsApi.registerBot(this);
            log.info("[STARTED] TelegramAPI. Bot Connected. Bot class: "+this);
        } catch (TelegramApiRequestException e) {
            log.error("Cannot connect. Pause "+ RECONNECT_PAUSE / 1000 + "sec and try again. Error "+ e.getMessage());
            try{
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return;
            }
            botConnect();
        }
    }
}
