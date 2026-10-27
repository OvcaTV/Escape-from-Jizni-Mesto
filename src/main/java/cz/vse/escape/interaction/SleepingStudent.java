package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFlag;
import cz.vse.escape.item.Item;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.world.LocationId;

/**
 * Spící student na chodbě 2. Za kávu prozradí, kde je páčidlo (to se objeví na WC ve 3. patře).
 * Od té chvíle se hráč nesmí vrátit do JM357A, jinak si ho Mandík podá za zpronevěru.
 */
public final class SleepingStudent extends Npc {

    public static final String PORTRAIT_ID = "student";

    public SleepingStudent() {
        super("spící student", "student", "spici student", "spici");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        if (game.getState().hasFlag(GameFlag.COFFEE_GIVEN_TO_STUDENT)) {
            game.say("Student se na chvilku zvedne z gauče: „Páčidlo je na záchodech ve 3. patře. "
                    + "A hlavně nechoď za Mandíkem...“ Pak zase zachrápe.");
        } else {
            game.say("Student spí na gauči a něco mumlá. Z jeho blábolení rozeznáš jen slovo „káva“.");
        }
    }

    @Override
    public boolean acceptsItem(Item item, Game game) {
        return item.id() == ItemId.COFFEE;
    }

    @Override
    public void useItem(Item item, Game game) {
        game.getState().getInventory().remove(ItemId.COFFEE);
        game.getState().setFlag(GameFlag.COFFEE_GIVEN_TO_STUDENT);
        game.getWorld().getLocation(LocationId.UPPER_TOILET).getItems()
                .add(game.getItemCatalog().get(ItemId.CROWBAR));
        game.say("Student se probudí, vděčně vypije kávu a zamumlá: „Na záchodech ve 3. patře najdeš páčidlo. "
                + "Na školníka jako dělané. Jenom se nevracej k Mandíkovi, kávu měl dostat on...“ "
                + "A znovu usne. (Do JM357A se už radši nevracej.)");
    }
}
