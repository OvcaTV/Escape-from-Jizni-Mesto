package cz.vse.escape.core;

/** Příznaky, do kterých si hra zapisuje, co se už stalo (stav questů a postav). Ukládají se spolu s hrou. */
public enum GameFlag {
    /** Hráč odevzdal semestrální práci, Mandík ho pustil na chodbu. */
    MANDIK_SATISFIED,
    /** Káva putovala k Mandíkovi (bez odměny). */
    COFFEE_GIVEN_TO_MANDIK,
    /** Káva putovala spícímu studentovi (páčidlo se objevilo na WC, do JM357A už nesmí). */
    COFFEE_GIVEN_TO_STUDENT,
    /** Školník je vyřešen (jakýmkoliv způsobem). */
    JANITOR_SOLVED,
    /** Školník byl omráčen páčidlem. */
    JANITOR_STUNNED,
    /** Hráč zahodil boty a chodí bos. */
    BAREFOOT
}
