package command;

public enum Command {
    NONE,       //Команды нет - возможно это ответ
    NOT_FOR_ME, //Команда не для нашего бота
    START,      // "/start"
    HELP,       // "/help"
    START_GAME, // "/start_game"

    LVL1,     //Ур. 1 - Европа
    LVL2,     //Ур. 2 - Европа + Азия
    LVL3,     //Ур. 3 - Европа + Азия + Африка
    LVL4,     //Ур. 4 - Европа + Азия + Африка + Америка
    LVL5,     //Ур. 5 - Европа + Азия + Африка + Америка + Австралия и океания

    GAME_OVER,  // "/stop_game"
    SAY_SCORE,  // "/score"
    ID,         // вернет айдишник юзера
    NOTIFY,
    STOP
}
