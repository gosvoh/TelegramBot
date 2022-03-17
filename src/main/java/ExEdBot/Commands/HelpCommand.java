package ExEdBot.Commands;

public class HelpCommand extends ServiceCommand {
    public HelpCommand() {
        super(Commands.HELP.getCommandIdentifier(), Commands.HELP.getCommandExtendedDescription());
    }
}
