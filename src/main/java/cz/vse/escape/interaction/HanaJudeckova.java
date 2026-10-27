package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;

/** Hana Judečková (studijní oddělení): do ničeho se nepouští a nechce být otravována. */
public final class HanaJudeckova extends Npc {

    public static final String PORTRAIT_ID = "hana";

    public HanaJudeckova() {
        super("Hana Judečková", "hana", "judeckova", "hana judeckova");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        game.say("Hana Judečková ani nezvedne oči od papírů: „Nemám čas, neotravujte mě. "
                + "Obraťte se na kolegyni.“");
    }
}
