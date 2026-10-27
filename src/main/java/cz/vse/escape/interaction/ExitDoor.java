package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.Item;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.text.StoryTexts;

/** Hlavní dveře z budovy. Otevřou se aktivovaným čipem, nebo se dají vypáčit páčidlem. */
public final class ExitDoor extends Prop {

    private static final String LOCKED_TEXT =
            "Dveře jsou zamčené a čtečka vedle nich nesvítí. Potřebuješ aktivovaný přístupový čip "
                    + "(nebo něco, čím by se dveře daly vypáčit).";

    public ExitDoor() {
        super("hlavní dveře", "dvere", "hlavni dvere", "dvere ven", "vychod");
    }

    @Override
    public boolean acceptsItem(Item item, Game game) {
        return item.id() == ItemId.CROWBAR || item.id() == ItemId.ACTIVE_CHIP;
    }

    @Override
    public void useItem(Item item, Game game) {
        if (item.id() == ItemId.CROWBAR) {
            game.getState().getInventory().remove(ItemId.CROWBAR);
            game.win(StoryTexts.WIN_WITH_CROWBAR);
        } else {
            game.win(StoryTexts.WIN_WITH_CHIP);
        }
    }

    /** Příkaz "odejit": projde dveřmi, pokud má hráč aktivovaný čip. */
    public void tryToLeaveBuilding(Game game) {
        if (game.getState().getInventory().contains(ItemId.ACTIVE_CHIP)) {
            game.win(StoryTexts.WIN_WITH_CHIP);
        } else {
            game.say(LOCKED_TEXT);
        }
    }
}
