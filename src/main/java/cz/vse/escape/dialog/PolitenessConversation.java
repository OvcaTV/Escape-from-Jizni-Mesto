package cz.vse.escape.dialog;

import cz.vse.escape.core.Game;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.text.StoryTexts;

import java.util.List;

/**
 * Rozhovor s referentkami: u každé odpovědi je skrytá hodnota slušnosti 1–3.
 * Na konci se body sečtou – při dosažení minima se čip aktivuje, jinak hráče vyhodí do kabinetu.
 */
public final class PolitenessConversation extends ChoiceConversation {

    private static final int MINIMUM_POINTS_TO_SUCCEED = 9;

    private static final List<ChoiceQuestion> QUESTIONS = List.of(
            new ChoiceQuestion(
                    "Jana Hudečková zvedne hlavu od monitoru: „Dobrý večer. Čím mohu posloužit?“",
                    List.of(
                            new ChoiceOption("Čau, potřebuju aktivovat čip, tak rychle.", 1,
                                    "Jana ti věnuje dlouhý pohled."),
                            new ChoiceOption("Dobrý večer, prosím vás, mohla byste mi aktivovat tenhle čip?", 3,
                                    "Jana se mírně usměje."),
                            new ChoiceOption("Dobrý večer, mám tu čip, aktivujte ho.", 2,
                                    "Jana zvedne obočí."))),
            new ChoiceQuestion(
                    "„A proč jste tu tak pozdě? Úřední hodiny už dávno skončily.“",
                    List.of(
                            new ChoiceOption("To vás nemusí zajímat, mám spěch.", 1,
                                    "Jana našpulí rty."),
                            new ChoiceOption("Zaspal jsem. Stává se.", 2,
                                    "Jana jen povzdechne."),
                            new ChoiceOption("Omlouvám se, usnul jsem na přednášce a snažím se dostat domů.", 3,
                                    "Jana pokývá hlavou, že chápe."))),
            new ChoiceQuestion(
                    "„Máte vyplněný formulář o aktivaci?“",
                    List.of(
                            new ChoiceOption("Formulář nemám, ale rád ho vyplním, pokud mi ho podáte.", 3,
                                    "Jana ti ochotně podává papír."),
                            new ChoiceOption("Jaký formulář? Tohle je zbytečná byrokracie.", 1,
                                    "Za přepážkou to zavrčí."),
                            new ChoiceOption("Formulář nemám, nešlo by to bez něj?", 2,
                                    "Jana pokrčí rameny."))),
            new ChoiceQuestion(
                    "„Tak a ještě poslední věc. Co říkáte na naši školu?“",
                    List.of(
                            new ChoiceOption("Je tu hezká katastrofa.", 1,
                                    "Hana Judečková si nahlas odfrkne."),
                            new ChoiceOption("Je to tu v pohodě, jen těžko se odsud dostává ven.", 2,
                                    "Jana se pousměje."),
                            new ChoiceOption("Škola je skvělá a vy na studijním pracujete obdivuhodně.", 3,
                                    "Jana se začervená."))));

    private int currentQuestionIndex;
    private int collectedPoints;

    @Override
    public void start(Game game) {
        askCurrentQuestion(game);
    }

    @Override
    protected void onOptionChosen(int optionIndex, Game game) {
        ChoiceOption chosen = QUESTIONS.get(currentQuestionIndex).options().get(optionIndex);
        collectedPoints += chosen.points();
        game.say(chosen.reaction());

        currentQuestionIndex++;
        if (currentQuestionIndex < QUESTIONS.size()) {
            askCurrentQuestion(game);
        } else {
            evaluate(game);
        }
    }

    private void askCurrentQuestion(Game game) {
        ChoiceQuestion question = QUESTIONS.get(currentQuestionIndex);
        showQuestion(game, question.prompt(), question.optionTexts());
    }

    private void evaluate(Game game) {
        if (collectedPoints >= MINIMUM_POINTS_TO_SUCCEED) {
            game.getState().getInventory().remove(ItemId.INACTIVE_CHIP);
            game.getState().getInventory().add(game.getItemCatalog().get(ItemId.ACTIVE_CHIP));
            game.endConversation();
            game.say("Jana Hudečková přiloží čip ke čtečce, ozve se pípnutí. „Hotovo. Čip je aktivovaný, "
                    + "hezký večer.“ Teď už stačí dojít k hlavním dveřím v přízemí.");
        } else {
            game.lose(StoryTexts.CLERKS_THROW_YOU_OUT);
        }
    }
}
