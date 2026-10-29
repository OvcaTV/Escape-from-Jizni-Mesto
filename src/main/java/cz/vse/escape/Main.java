package cz.vse.escape;

import cz.vse.escape.core.GameConsole;
import cz.vse.escape.core.GameFactory;
import cz.vse.escape.core.GameStatus;
import cz.vse.escape.core.StandardGameConsole;
import cz.vse.escape.text.TextNormalizer;

import java.util.Set;

/** Spouštěč hry | Po výhře nebo prohře nabídne novou hru. */
public final class Main {

    private static final Set<String> YES_ANSWERS = Set.of("ano", "a", "y", "yes");

    private Main() {
    }

    public static void main(String[] args) {
        GameConsole console = new StandardGameConsole();
        boolean playAgain;
        do {
            GameStatus result = GameFactory.createNewGame(console).run();
            playAgain = result != GameStatus.QUIT && wantsToPlayAgain(console);
        } while (playAgain);
        console.println("Díky za hru!");
    }

    private static boolean wantsToPlayAgain(GameConsole console) {
        String answer = console.readLine("Chceš hrát znovu? (ano/ne) ");
        return answer != null && YES_ANSWERS.contains(TextNormalizer.normalize(answer));
    }
}
