package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.Item;
import cz.vse.escape.text.Messages;

import java.util.Optional;

/** prozkoumej [předmět] – znovu popíše místnost, nebo (s argumentem) předmět v inventáři či na zemi. */
public final class LookAroundCommand implements Command {

    @Override
    public String getName() {
        return "prozkoumej";
    }

    @Override
    public String getUsage() {
        return "prozkoumej [předmět]";
    }

    @Override
    public String getDescription() {
        return "znovu vypíše, kde jsi, co tu vidíš a kdo tu stojí; s názvem předmětu ho popíše";
    }

    @Override
    public void execute(String arguments, Game game) {
        if (arguments.isBlank()) {
            game.describeCurrentLocation();
            return;
        }
        Optional<Item> item = game.getState().getInventory().findByName(arguments)
                .or(() -> game.getState().getCurrentLocation().getItems().findByName(arguments));
        game.say(item.map(Item::description).orElse(Messages.NOTHING_VISIBLE));
    }
}
