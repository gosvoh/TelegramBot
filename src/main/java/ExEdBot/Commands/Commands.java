package ExEdBot.Commands;

import lombok.Getter;

public enum Commands {
    AUTHENTICATION("auth", "Аутентификация", "Команда аутентификации"),
    HELP("help", "Помощь", "Команда помощи"),
    UPLOAD("upload", "Загрузить словарь", "Команда загрузки"),
    START("start", "Старт", "Давайте начнём! Если Вам нужна помощь, нажмите соответствующую кнопку на клавиатуре");

    @Getter
    private final String commandIdentifier;
    @Getter
    private final String commandBtnName;
    @Getter
    private final String commandExtendedDescription;

    Commands(String commandIdentifier, String commandBtnName, String commandExtendedDescription) {
        this.commandIdentifier = commandIdentifier;
        this.commandBtnName = commandBtnName;
        this.commandExtendedDescription = commandExtendedDescription;
    }

    public static Commands getIdentifier(String desc) {
        for (var o : Commands.values()) {
            if (o.getCommandBtnName().equals(desc))
                return o;
        }
        return null;
    }
}
