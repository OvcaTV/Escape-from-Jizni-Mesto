package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;

/**
 * Postava ve hře. Při oslovení se nejdřív zobrazí její ASCII portrét ({@link #portraitId()}),
 * potom postava reaguje v {@link #converse(Game)}.
 */
public abstract class Npc extends Interactable {

    protected Npc(String displayName, String... aliases) {
        super(displayName, aliases);
    }

    /** Id souboru s ASCII grafikou ({@code ascii/<id>.txt}). */
    protected abstract String portraitId();

    @Override
    public final void talk(Game game) {
        game.showPortrait(portraitId());
        converse(game);
    }

    /** Reakce postavy na oslovení (portrét už je vypsaný). */
    protected abstract void converse(Game game);
}
