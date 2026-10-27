package cz.vse.escape.dialog;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.ItemId;

import java.util.List;

/**
 * Ing. Chlapek se ptá na SQL. Při špatné odpovědi se ptá znovu,
 * při správné dá hráči neaktivní přístupový čip.
 */
public final class SqlQuizConversation extends ChoiceConversation {

    private static final int CORRECT = 1;
    private static final int WRONG = 0;

    private static final ChoiceQuestion QUESTION = new ChoiceQuestion(
            "Ing. Chlapek, Ph.D. na tebe přísně pohlédne: „Když už tu jste, pane kolego – jaký je správný SQL "
                    + "dotaz, který vypíše jména všech studentů starších 20 let z tabulky STUDENT?“",
            List.of(
                    new ChoiceOption("SELECT jmeno WHERE vek > 20 FROM student;", WRONG,
                            "„To není ani syntakticky správně. Zkuste to znovu.“"),
                    new ChoiceOption("SELECT jmeno FROM student WHERE vek > 20;", CORRECT, ""),
                    new ChoiceOption("SELECT * FROM student ORDER BY vek > 20;", WRONG,
                            "„To vypíše všechny studenty a všechny sloupce. Zkuste to znovu.“")));

    @Override
    public void start(Game game) {
        showQuestion(game, QUESTION.prompt(), QUESTION.optionTexts());
    }

    @Override
    protected void onOptionChosen(int optionIndex, Game game) {
        ChoiceOption chosen = QUESTION.options().get(optionIndex);
        if (chosen.points() == CORRECT) {
            game.getState().getInventory().add(game.getItemCatalog().get(ItemId.INACTIVE_CHIP));
            game.endConversation();
            game.say("„Správně,“ pokývá Chlapek hlavou a podá ti přístupový čip od hlavních dveří. „Aby fungoval, "
                    + "musíte ho nechat aktivovat na studijním oddělení ve 2. patře. A buďte tam slušný.“");
        } else {
            game.say(chosen.reaction());
            showQuestion(game, QUESTION.prompt(), QUESTION.optionTexts());
        }
    }
}
