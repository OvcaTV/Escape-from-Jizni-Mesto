package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.text.Messages;
import cz.vse.escape.world.Exit;

import java.util.List;

/** jdi [lokace] – přesun do sousední místnosti. */
public final class GoCommand implements Command {

    @Override
    public String getName() {
        return "jdi";
    }

    @Override
    public String getUsage() {
        return "jdi [lokace]";
    }

    @Override
    public String getDescription() {
        return "přesun do jiné místnosti (např. jdi chodba 2, jdi JM104)";
    }

    @Override
    public void execute(String arguments, Game game) {
        if (arguments.isBlank()) {
            game.say(Messages.ASK_WHERE_TO_GO);
            return;
        }
        List<Exit> matchingExits = game.getState().getCurrentLocation().findExits(arguments);
        if (matchingExits.isEmpty()) {
            game.say(Messages.NO_WAY_THERE);
        } else if (matchingExits.size() > 1) {
            game.say(Messages.AMBIGUOUS_DESTINATION);
        } else {
            game.enter(matchingExits.get(0).getTarget());
        }
    }
}
