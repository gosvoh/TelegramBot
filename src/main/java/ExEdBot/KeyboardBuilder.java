package ExEdBot;

import ExEdBot.Commands.Commands;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardBuilder extends ReplyKeyboardMarkup {
    public KeyboardBuilder(Commands... commands) {
        super(buildKeyboardRows(commands), true, false, true, "Placeholder");
    }

    private static List<KeyboardRow> buildKeyboardRows(Commands[] commands) {
        List<KeyboardRow> rows = new ArrayList<>();
        int numberOfColumns = 3;
        for (int i = 0; i < commands.length / numberOfColumns + commands.length % numberOfColumns; i++)
            rows.add(new KeyboardRow());
        for (int i = 0; i < commands.length; i++)
            rows.get(i / numberOfColumns).add(commands[i].getCommandBtnName());

        return rows;
    }
}
