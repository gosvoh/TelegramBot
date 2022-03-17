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
        for (int i = 0; i < commands.length / 2 + commands.length % 2; i++)
            rows.add(new KeyboardRow());
        for (int i = 0; i < commands.length; i++)
            rows.get(i % 2).add(commands[i].getCommandBtnName());

        return rows;
    }
}
