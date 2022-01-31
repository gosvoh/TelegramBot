package command;

public enum Command {
    NONE,       //Команды нет
    NOT_FOR_ME, //Команда не для нашего бота
    START,      // "/start"
    HELP,       // "/help"
    START_GAME, // "/start_game"
    GAME_OVER,  // "/stop_game"
    SAY_SCORE,  // "/score"
    ID,         // вернет айдишник юзера
    NOTIFY
}
