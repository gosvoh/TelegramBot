package service;

import bot.Bot;

import command.Command;
import command.ParsedCommand;
import command.Parser;
import handlers.*;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import handlers.AbstractHandler;
import handlers.DefaultHandler;
import handlers.SystemHandler;

import java.io.IOException;


public class MessageReceiver implements Runnable {
    private static final Logger log = Logger.getLogger(MessageReceiver.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private Bot bot;
    private Parser parser;

    public MessageReceiver(Bot bot) {
        this.bot=bot;
        parser = new Parser(bot.getBotUsername());
    }


    @Override
    public void run() {

        log.info("[STARTED] MsgReceiver.  Bot class: " + bot);
        while (true) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                log.debug("New object for analyze in queue " + object.toString());
                try {
                    analyze(object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }

    //Обработка типа объекта который принимает бот
    private void analyze(Object object) throws IOException {
        if(object instanceof Update){
            Update update = (Update) object;
            log.debug("Update received: "+update.toString());
            analyzeForUpdateType(update);
        } else{
            log.warn("Cant operate type of object: "+object.toString());
        }
    }

    private void analyzeForUpdateType(Update update) throws IOException {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if(!"".equals(operationResult)){
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(operationResult);
            bot.sendQueue.add(message);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command){
        if (command == null){
            log.warn("Null command accepted. This is not god scenario. ");
            return new DefaultHandler(bot);
        }
        switch (command){
            case START_GAME:
                GameHandler gameHandler = new GameHandler(bot);
                log.info("Handler for command["+ command.toString()+"] is: "+gameHandler);
                return gameHandler;
            case START:
            case HELP:
            case ID:
                SystemHandler systemHandler = new SystemHandler(bot);
                log.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(bot);
                log.info("Handler for command[" + command.toString() + "] is: " + notifyHandler);
                return notifyHandler;
            //case SAY_SCORE:
            //case GAME_OVER:
            default:
                log.info("Handler for command[" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot);
        }
    }
}
