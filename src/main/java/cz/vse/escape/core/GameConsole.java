package cz.vse.escape.core;

/** Vstup a výstup hry. Oddělený od hry, aby šla hra snadno testovat skriptovaným vstupem. */
public interface GameConsole {

    void println(String text);

    /** Vypíše výzvu a přečte řádek. Vrací null, pokud už není další vstup. */
    String readLine(String prompt);
}
