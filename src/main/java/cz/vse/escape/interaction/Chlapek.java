package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.dialog.SqlQuizConversation;
import cz.vse.escape.item.ItemId;

/** Ing. Dušan Chlapek, Ph.D. (JM104): za správnou odpověď na SQL otázku dá neaktivní přístupový čip. */
public final class Chlapek extends Npc {

    public static final String PORTRAIT_ID = "chlapek";

    public Chlapek() {
        super("Ing. Chlapek", "chlapek", "ing chlapek", "dusan chlapek", "ing. dusan chlapek");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        boolean alreadyHasChip = game.getState().getInventory().contains(ItemId.INACTIVE_CHIP)
                || game.getState().getInventory().contains(ItemId.ACTIVE_CHIP);
        if (alreadyHasChip) {
            game.say("Chlapek na tebe ani nevzhlédne: „Čip už máte. Aktivovat ho půjdete na studijní oddělení "
                    + "do 2. patra.“");
        } else {
            game.startConversation(new SqlQuizConversation());
        }
    }
}
