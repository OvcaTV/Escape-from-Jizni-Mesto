package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.save.SaveGameRepository;
import cz.vse.escape.text.Messages;
import cz.vse.escape.text.TextNormalizer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Zná všechny příkazy hry a podle prvního slova řádku vybere ten správný. */
public final class CommandRegistry {

    private final Map<String, Command> commandsByName = new LinkedHashMap<>();
    private final Map<String, Command> commandsByAlias = new LinkedHashMap<>();

    public void register(Command command) {
        commandsByName.put(TextNormalizer.normalize(command.getName()), command);
        for (String alias : command.getAliases()) {
            commandsByAlias.put(TextNormalizer.normalize(alias), command);
        }
    }

    /** Příkazy v pořadí registrace (používá nápověda). */
    public Collection<Command> getAll() {
        return Collections.unmodifiableCollection(commandsByName.values());
    }

    public void execute(String rawInput, Game game) {
        String normalizedInput = TextNormalizer.normalize(rawInput);
        int firstSpace = normalizedInput.indexOf(' ');
        String commandName = firstSpace < 0 ? normalizedInput : normalizedInput.substring(0, firstSpace);
        String arguments = firstSpace < 0 ? "" : normalizedInput.substring(firstSpace + 1).trim();

        Command command = commandsByName.getOrDefault(commandName, commandsByAlias.get(commandName));
        if (command == null) {
            game.say(Messages.UNKNOWN_COMMAND);
            return;
        }
        command.execute(arguments, game);
    }

    /** Sada příkazů pro hru. Nový příkaz = nová třída implementující {@link Command} + jeden řádek tady. */
    public static CommandRegistry createStandard() {
        SaveGameRepository saveGameRepository = new SaveGameRepository();

        CommandRegistry registry = new CommandRegistry();
        registry.register(new GoCommand());
        registry.register(new TakeCommand());
        registry.register(new UseCommand());
        registry.register(new TalkCommand());
        registry.register(new InventoryCommand());
        registry.register(new LookAroundCommand());
        registry.register(new LeaveBuildingCommand());
        registry.register(new SaveCommand(saveGameRepository));
        registry.register(new LoadCommand(saveGameRepository));
        registry.register(new HelpCommand(registry));
        registry.register(new QuitCommand());
        return registry;
    }
}
