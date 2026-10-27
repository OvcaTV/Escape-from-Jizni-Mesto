package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.Item;
import cz.vse.escape.item.ItemCollection;
import cz.vse.escape.text.Messages;

import java.util.Optional;

/** seber [předmět] – přesune předmět z místnosti do inventáře. */
public final class TakeCommand implements Command {

    @Override
    public String getName() {
        return "seber";
    }

    @Override
    public String getUsage() {
        return "seber [předmět]";
    }

    @Override
    public String getDescription() {
        return "vložení věci z místnosti do inventáře (např. seber flashdisk)";
    }

    @Override
    public void execute(String arguments, Game game) {
        if (arguments.isBlank()) {
            game.say(Messages.ASK_WHAT_TO_TAKE);
            return;
        }
        ItemCollection itemsInLocation = game.getState().getCurrentLocation().getItems();
        Optional<Item> item = itemsInLocation.findByName(arguments);
        if (item.isEmpty()) {
            game.say(Messages.NOTHING_TO_TAKE);
            return;
        }
        itemsInLocation.remove(item.get().id());
        game.getState().getInventory().add(item.get());
        game.say("Sebral jsi: " + item.get().displayName() + ".");
    }
}
