package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.text.Messages;

/** konec – ukončí hru. */
public final class QuitCommand implements Command {

    @Override
    public String getName() {
        return "konec";
    }

    @Override
    public String getUsage() {
        return "konec";
    }

    @Override
    public String getDescription() {
        return "ukončí hru";
    }

    @Override
    public void execute(String arguments, Game game) {
        game.say(Messages.GAME_QUIT);
        game.quit();
    }
}
