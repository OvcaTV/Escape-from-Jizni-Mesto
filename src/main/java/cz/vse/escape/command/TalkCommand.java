package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.interaction.Interactable;
import cz.vse.escape.text.Messages;

import java.util.Optional;

/** mluv [postava] – zahájí rozhovor nebo vyvolá reakci postavy. */
public final class TalkCommand implements Command {

    @Override
    public String getName() {
        return "mluv";
    }

    @Override
    public String getUsage() {
        return "mluv [postava]";
    }

    @Override
    public String getDescription() {
        return "zahájení konverzace (např. mluv mandik, mluv referentky)";
    }

    @Override
    public void execute(String arguments, Game game) {
        if (arguments.isBlank()) {
            game.say(Messages.ASK_WHO_TO_TALK_TO);
            return;
        }
        Optional<Interactable> target = game.getState().getCurrentLocation().findInteractable(arguments);
        if (target.isEmpty()) {
            game.say(Messages.NOBODY_LIKE_THAT_HERE);
        } else {
            target.get().talk(game);
        }
    }
}
