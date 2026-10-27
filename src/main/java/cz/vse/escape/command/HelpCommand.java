package cz.vse.escape.command;

import cz.vse.escape.core.Game;

/** napoveda – seznam příkazů. */
public final class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getName() {
        return "napoveda";
    }

    @Override
    public String getUsage() {
        return "napoveda";
    }

    @Override
    public String getDescription() {
        return "zobrazí seznam použitelných příkazů";
    }

    @Override
    public void execute(String arguments, Game game) {
        StringBuilder text = new StringBuilder("Použitelné příkazy:");
        for (Command command : registry.getAll()) {
            text.append("\n  ").append(command.getUsage()).append(" – ").append(command.getDescription());
        }
        text.append("\nV rozhovorech odpovídáš jen písmenem A, B nebo C.");
        game.say(text.toString());
    }
}
