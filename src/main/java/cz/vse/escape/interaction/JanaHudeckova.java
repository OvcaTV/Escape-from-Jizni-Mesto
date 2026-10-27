package cz.vse.escape.interaction;

import cz.vse.escape.core.Game;
import cz.vse.escape.dialog.PolitenessConversation;
import cz.vse.escape.item.ItemId;

/** Jana Hudečková (studijní oddělení): aktivuje čip, pokud je student dostatečně slušný. */
public final class JanaHudeckova extends Npc {

    public static final String PORTRAIT_ID = "jana";

    public JanaHudeckova() {
        super("Jana Hudečková", "jana", "hudeckova", "jana hudeckova", "referentky", "referentka");
    }

    @Override
    protected String portraitId() {
        return PORTRAIT_ID;
    }

    @Override
    protected void converse(Game game) {
        if (game.getState().getInventory().contains(ItemId.ACTIVE_CHIP)) {
            game.say("Jana Hudečková se usměje: „Čip už máte aktivní, tak ať vám slouží. Hezký večer.“");
        } else if (!game.getState().getInventory().contains(ItemId.INACTIVE_CHIP)) {
            game.say("Jana Hudečková: „Bez čipu k aktivaci tu nemáte co dělat. Zkuste to v přednáškové "
                    + "místnosti v přízemí.“");
        } else {
            game.startConversation(new PolitenessConversation());
        }
    }
}
