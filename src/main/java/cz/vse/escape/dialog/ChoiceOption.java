package cz.vse.escape.dialog;

/**
 * Jedna odpověď v dialogu ABC.
 *
 * @param text     znění odpovědi, jak ho vidí hráč
 * @param points   skrytá hodnota odpovědi (jak ji vyhodnotí konkrétní rozhovor – správnost, slušnost...)
 * @param reaction co na odpověď řekne postava
 */
public record ChoiceOption(String text, int points, String reaction) {
}
