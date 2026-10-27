package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.save.SaveGameException;
import cz.vse.escape.save.SaveGameRepository;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/** uloz [soubor] – uloží rozehranou hru. */
public final class SaveCommand implements Command {

    static final String DEFAULT_FILE_NAME = "savegame.properties";

    private final SaveGameRepository repository;

    public SaveCommand(SaveGameRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getName() {
        return "uloz";
    }

    @Override
    public String getUsage() {
        return "uloz [soubor]";
    }

    @Override
    public String getDescription() {
        return "uloží rozehranou hru (výchozí soubor " + DEFAULT_FILE_NAME + ")";
    }

    @Override
    public void execute(String arguments, Game game) {
        try {
            Path file = Path.of(arguments.isBlank() ? DEFAULT_FILE_NAME : arguments);
            repository.save(game, file);
            game.say("Hra byla uložena do souboru " + file + ".");
        } catch (SaveGameException exception) {
            game.say(exception.getMessage());
        } catch (InvalidPathException exception) {
            game.say("Neplatný název souboru.");
        }
    }
}
