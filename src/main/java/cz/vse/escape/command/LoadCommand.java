package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.save.SaveGameException;
import cz.vse.escape.save.SaveGameRepository;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/** nacti [soubor] – načte dříve uloženou hru. */
public final class LoadCommand implements Command {

    private final SaveGameRepository repository;

    public LoadCommand(SaveGameRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getName() {
        return "nacti";
    }

    @Override
    public String getUsage() {
        return "nacti [soubor]";
    }

    @Override
    public String getDescription() {
        return "načte uloženou hru (výchozí soubor " + SaveCommand.DEFAULT_FILE_NAME + ")";
    }

    @Override
    public void execute(String arguments, Game game) {
        try {
            Path file = Path.of(arguments.isBlank() ? SaveCommand.DEFAULT_FILE_NAME : arguments);
            repository.load(game, file);
            game.say("Hra byla načtena ze souboru " + file + ".");
            game.describeCurrentLocation();
        } catch (SaveGameException exception) {
            game.say(exception.getMessage());
        } catch (InvalidPathException exception) {
            game.say("Neplatný název souboru.");
        }
    }
}
