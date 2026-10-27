package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFlag;
import cz.vse.escape.dialog.JanitorEncounter;

/** Školník na chodbě do 2. části. Požaduje přezutí; setkání s ním řeší {@link JanitorEncounter}. */
public final class Janitor extends Npc {

    public static final String PORTRAIT_ID = "skolnik";

    public Janitor() {
        super("školník", "skolnik", "skolnik budovy");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        if (!game.getState().hasFlag(GameFlag.JANITOR_SOLVED)) {
            game.startConversation(new JanitorEncounter());
        } else if (game.getState().hasFlag(GameFlag.JANITOR_STUNNED)) {
            game.say("Školník leží na zemi a tiše chrápe. Radši ho nebudit.");
        } else {
            game.say("Školník si tvé boty zkoumavě prohlíží: „No vida. Příště přezuj hned.“");
        }
    }
}
