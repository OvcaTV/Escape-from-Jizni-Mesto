package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.ItemCollection;
import cz.vse.escape.text.Messages;

/** inventar – vypíše, co hráč nese. */
public final class InventoryCommand implements Command {

    @Override
    public String getName() {
        return "inventar";
    }

    @Override
    public String getUsage() {
        return "inventar";
    }

    @Override
    public String getDescription() {
        return "vypíše seznam předmětů, které máš u sebe";
    }

    @Override
    public void execute(String arguments, Game game) {
        ItemCollection inventory = game.getState().getInventory();
        if (inventory.isEmpty()) {
            game.say(Messages.EMPTY_INVENTORY);
        } else {
            game.say("U sebe máš: " + inventory.describe() + ".");
        }
    }
}
