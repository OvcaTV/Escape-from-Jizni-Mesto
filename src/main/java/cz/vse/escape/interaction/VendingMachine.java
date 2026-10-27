package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.Item;
import cz.vse.escape.item.ItemId;

/** Automat na kávu: za peníze vydá kávu. */
public final class VendingMachine extends Prop {

    public VendingMachine() {
        super("automat na kávu", "automat", "automat na kavu", "kavovar");
    }

    @Override
    public boolean acceptsItem(Item item, Game game) {
        return item.id() == ItemId.COFFEE_MONEY;
    }

    @Override
    public void useItem(Item item, Game game) {
        game.getState().getInventory().remove(ItemId.COFFEE_MONEY);
        game.getState().getInventory().add(game.getItemCatalog().get(ItemId.COFFEE));
        game.say("Vhodil jsi mince do automatu. Automat zabzučel, zachrčel a vyplivl horký kelímek kávy. "
                + "Peníze zmizely, káva je tvoje.");
    }
}
