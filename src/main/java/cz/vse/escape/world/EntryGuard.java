package cz.vse.escape.world;

import cz.vse.escape.core.GameState;

import java.util.Optional;

/** Podmínka vstupu do místnosti. Vrací důvod, proč hráč nesmí dál, nebo prázdnou hodnotu, pokud smí. */
@FunctionalInterface
public interface EntryGuard {

    Optional<String> blockReason(GameState state);
}
