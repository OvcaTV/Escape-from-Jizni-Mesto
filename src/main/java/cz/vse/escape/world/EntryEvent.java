package cz.vse.escape.world;

import cz.vse.escape.core.Game;

/** Událost, která se spustí po vstupu hráče do místnosti (po výpisu jejího popisu). */
@FunctionalInterface
public interface EntryEvent {

    void onEntered(Game game);
}
