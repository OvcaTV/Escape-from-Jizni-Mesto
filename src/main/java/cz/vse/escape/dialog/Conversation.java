package cz.vse.escape.dialog;

import cz.vse.escape.core.Game;

/**
 * Rozhovor, během kterého hráč nezadává běžné příkazy, ale jen odpovědi.
 * Rozhovor skončí zavoláním {@link Game#endConversation()} (nebo koncem hry).
 */
public interface Conversation {

    /** Vypíše úvod a první otázku. */
    void start(Game game);

    /** Zpracuje odpověď hráče. */
    void handleInput(String input, Game game);
}
