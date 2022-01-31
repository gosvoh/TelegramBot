package command;

import javafx.util.Pair;
import org.apache.log4j.Logger;

public class Parser {
    private static final Logger log = Logger.getLogger(Parser.class);
    private final String PREFIX_FOR_COMMAND = "/";
    private final String DELIMITER_COMMAND_BOT_NAME = "@";
    private final String botName;

    public Parser(String botName) {
        this.botName = botName;
    }

    //Возвращает готовую команду.
    public ParsedCommand getParsedCommand(String text) {
        String trimText = "";
        if (text != null) trimText = text.trim();
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);

        if("".equals(trimText)) return  result;
        Pair<String, String> commandAndText = getDelimitedCommandFromText(trimText);

        if (isCommand(commandAndText.getKey())) {
            if (isCommandForMe(commandAndText.getKey())) {
                String commandForParse = cutCommandFromFullText(commandAndText.getKey());
                Command commandFromText = getCommandFromText(commandForParse);
                result.setText(commandAndText.getValue());
                result.setCommand(commandFromText);
            } else {
                result.setCommand(Command.NOT_FOR_ME);
                result.setText(commandAndText.getValue());
            }

        }
        return result;
    }

    //Возвращает текст без вырезанной команды.
    private String cutCommandFromFullText(String text){
        return text.contains(DELIMITER_COMMAND_BOT_NAME) ? text.substring(1,text.indexOf(DELIMITER_COMMAND_BOT_NAME)): text.substring(1);
    }

    //Возвращает пару текст до пробела и текст после пробела.
    private Pair<String, String> getDelimitedCommandFromText(String trimText){
        Pair<String, String> commandText;

        if(trimText.contains(" ")){
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new Pair<>(trimText.substring(0,indexOfSpace),trimText.substring(indexOfSpace+1)); // [от первого - " "],[следующий индекс после пробела - последний индекс]
        }else commandText = new Pair<>(trimText, "");

        return commandText;
    }

    //Возвращает тип класса перечислений команд, или сообщает об ошибке.
    private Command getCommandFromText(String text){
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.NONE;
        try{
            command = Command.valueOf(upperCaseText);
        }catch (IllegalArgumentException e){
            log.debug("Не могу распарсить команду: "+text);
        }
        return command;
    }

    //Проверка найдена ли строка начинающаяся с определенного символа.
    private boolean isCommand(String command){
        return command.startsWith(PREFIX_FOR_COMMAND);
    }

    //Проверка, найденная строка содержит ли имя нашего бота и разделитель.
    private boolean isCommandForMe(String command){
        if(command.contains(DELIMITER_COMMAND_BOT_NAME)){
            String botNameForEqual = command.substring(command.indexOf(DELIMITER_COMMAND_BOT_NAME)+1);
            return botName.equals(botNameForEqual);
        }
        return true;
    }

    private boolean isAnswer(String text){
        if(!text.contains(PREFIX_FOR_COMMAND)){
            return true;
        }
        return false;
    }

}
