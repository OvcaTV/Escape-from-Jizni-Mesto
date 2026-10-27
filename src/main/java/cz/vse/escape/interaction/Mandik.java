package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFlag;
import cz.vse.escape.core.GameState;
import cz.vse.escape.item.Item;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.text.StoryTexts;

/**
 * Ing. Mandík (JM357A): za semestrální práci pustí hráče na chodbu a dá mu peníze na kávu.
 * Kávu si může nechat donést, ale žádnou odměnu za ni nedá.
 */
public final class Mandik extends Npc {

    public static final String PORTRAIT_ID = "mandik";

    public Mandik() {
        super("Ing. Mandík", "mandik", "ing mandik", "ondrej mandik", "ing. ondrej mandik");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        GameState state = game.getState();
        if (!state.hasFlag(GameFlag.MANDIK_SATISFIED)) {
            game.say(StoryTexts.MANDIK_BLOCKS_THE_WAY);
        } else if (state.hasFlag(GameFlag.COFFEE_GIVEN_TO_MANDIK)) {
            game.say("Mandík upíjí kávu: „Výborná. A teď mě nechte, ještě mám co opravovat.“");
        } else {
            game.say("Mandík netrpělivě zvedne obočí: „Tak co, kde mám tu kávu? Automat je dole ve 2. patře.“");
        }
    }

    @Override
    public boolean acceptsItem(Item item, Game game) {
        return item.id() == ItemId.USB_FLASH_DRIVE || item.id() == ItemId.COFFEE;
    }

    @Override
    public void useItem(Item item, Game game) {
        if (item.id() == ItemId.USB_FLASH_DRIVE) {
            acceptSemesterProject(game);
        } else {
            acceptCoffee(game);
        }
    }

    private void acceptSemesterProject(Game game) {
        game.getState().getInventory().remove(ItemId.USB_FLASH_DRIVE);
        game.getState().getInventory().add(game.getItemCatalog().get(ItemId.COFFEE_MONEY));
        game.getState().setFlag(GameFlag.MANDIK_SATISFIED);
        game.say("Odevzdal jsi USB flashdisk Ing. Mandíkovi. „Výborně,“ říká Mandík. „Tady máte peníze, "
                + "skočte mi do automatu pro kávu.“ Cesta na chodbu je nyní volná. Flashdisk zmizel z inventáře.");
    }

    private void acceptCoffee(Game game) {
        game.getState().getInventory().remove(ItemId.COFFEE);
        game.getState().setFlag(GameFlag.COFFEE_GIVEN_TO_MANDIK);
        game.say("Mandík si kávu vezme a spokojeně ji usrkne: „Děkuji, to jste hodný.“ Žádnou odměnu ti za ni "
                + "ale nedá. Ten, kdo ví, kde je páčidlo, bude asi někdo jiný...");
    }
}
