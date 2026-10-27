package cz.vse.escape.dialog;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFlag;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.text.StoryTexts;

import java.util.List;

/**
 * Setkání se školníkem: hráč volí mezi omráčením páčidlem, zahozením bot a ignorováním.
 * Ignorování znamená konec hry.
 */
public final class JanitorEncounter extends ChoiceConversation {

    private static final int USE_CROWBAR = 0;
    private static final int TAKE_OFF_SHOES = 1;
    private static final int IGNORE_JANITOR = 2;

    private static final String PROMPT =
            "Na chodbě stojí školník s kyblíkem a svazkem klíčů. Změří si tě přes brýle a zahřmí: "
                    + "„Kolman, kde je přezutí?!“";
    private static final List<String> OPTIONS = List.of(
            "Vytáhnout páčidlo a školníka omráčit",
            "Pokorně si sundat boty a zahodit je",
            "Předstírat, že školníka neslyšíš, a jít dál");

    @Override
    public void start(Game game) {
        showQuestion(game, PROMPT, OPTIONS);
    }

    @Override
    protected void onOptionChosen(int optionIndex, Game game) {
        switch (optionIndex) {
            case USE_CROWBAR -> stunJanitor(game);
            case TAKE_OFF_SHOES -> takeOffShoes(game);
            case IGNORE_JANITOR -> game.lose(StoryTexts.JANITOR_DRAGS_YOU_AWAY);
            default -> throw new IllegalStateException("Neznámá volba: " + optionIndex);
        }
    }

    private void stunJanitor(Game game) {
        if (!game.getState().getInventory().contains(ItemId.CROWBAR)) {
            game.say("Páčidlo u sebe nemáš. Školník se tváří čím dál podezřívavěji, vyber si jinak.");
            showQuestion(game, PROMPT, OPTIONS);
            return;
        }
        game.getState().getInventory().remove(ItemId.CROWBAR);
        game.getState().setFlag(GameFlag.JANITOR_SOLVED);
        game.getState().setFlag(GameFlag.JANITOR_STUNNED);
        game.endConversation();
        game.say("Rázně jsi školníka praštil páčidlem. Skácel se k zemi jako pytel brambor. Páčidlo se ti při úderu "
                + "zlomilo, ale boty máš pořád na nohou a cesta je volná.");
    }

    private void takeOffShoes(Game game) {
        game.getState().getInventory().remove(ItemId.SHOES);
        game.getState().setFlag(GameFlag.JANITOR_SOLVED);
        game.getState().setFlag(GameFlag.BAREFOOT);
        game.endConversation();
        game.say("Potupně sis sundal boty a hodil je školníkovi k nohám. „No vida, a jde to,“ zabručí školník "
                + "a uvolní ti cestu. Dohraješ bos.");
    }
}
