package cz.vse.escape.dialog;

import cz.vse.escape.core.Game;

import java.util.List;
import java.util.OptionalInt;

/**
 * Základ rozhovorů ve stylu ABC: vypíše otázku s nabídkou, přijímá jen písmeno odpovědi
 * a výběr předá potomkovi v {@link #onOptionChosen(int, Game)}.
 */
public abstract class ChoiceConversation implements Conversation {

    private static final char FIRST_OPTION_LETTER = 'A';

    private int currentOptionCount;

    protected final void showQuestion(Game game, String prompt, List<String> optionTexts) {
        currentOptionCount = optionTexts.size();
        game.say(prompt);
        for (int index = 0; index < optionTexts.size(); index++) {
            game.say("  " + letterOf(index) + ") " + optionTexts.get(index));
        }
    }

    @Override
    public final void handleInput(String input, Game game) {
        OptionalInt chosenIndex = parseOptionIndex(input);
        if (chosenIndex.isEmpty()) {
            game.say("Odpověz prosím písmenem " + letterOf(0) + " až " + letterOf(currentOptionCount - 1) + ".");
            return;
        }
        onOptionChosen(chosenIndex.getAsInt(), game);
    }

    /** Volá se po platné odpovědi; index 0 = A, 1 = B, 2 = C. */
    protected abstract void onOptionChosen(int optionIndex, Game game);

    private OptionalInt parseOptionIndex(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.length() != 1) {
            return OptionalInt.empty();
        }
        int index = Character.toUpperCase(trimmedInput.charAt(0)) - FIRST_OPTION_LETTER;
        boolean isValid = index >= 0 && index < currentOptionCount;
        return isValid ? OptionalInt.of(index) : OptionalInt.empty();
    }

    private char letterOf(int optionIndex) {
        return (char) (FIRST_OPTION_LETTER + optionIndex);
    }
}
