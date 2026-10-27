package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.Item;
import cz.vse.escape.text.Messages;
import cz.vse.escape.text.NameSet;

/**
 * Cokoliv v místnosti, s čím může hráč mluvit nebo na čem může použít předmět
 * (postavy {@link Npc} a věci v prostředí {@link Prop}).
 */
public abstract class Interactable {

    private final String displayName;
    private final NameSet names;

    protected Interactable(String displayName, String... aliases) {
        this.displayName = displayName;
        this.names = new NameSet(displayName, aliases);
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isCalled(String userInput) {
        return names.matchesExactly(userInput);
    }

    public boolean isCalledByPrefix(String userInput) {
        return names.matchesPrefix(userInput);
    }

    /** Reakce na "mluv [jméno]". Ve výchozím stavu s věcí mluvit nejde. */
    public void talk(Game game) {
        game.say(Messages.CANNOT_DO_THAT_NOW);
    }

    /** Říká, jestli má použití tohoto předmětu na tuto věc nějaký účinek. */
    public boolean acceptsItem(Item item, Game game) {
        return false;
    }

    /** Provede účinek předmětu. Volá se jen tehdy, když {@link #acceptsItem} vrátilo true. */
    public void useItem(Item item, Game game) {
        game.say(Messages.ITEM_HAS_NO_EFFECT);
    }
}
