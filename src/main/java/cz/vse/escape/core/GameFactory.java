package cz.vse.escape.core;

import cz.vse.escape.command.CommandRegistry;
import cz.vse.escape.item.ItemCatalog;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.world.World;
import cz.vse.escape.world.WorldFactory;

/** Sestaví novou hru: svět, výchozí stav hráče (v inventáři má boty) a příkazy. */
public final class GameFactory {

    private GameFactory() {
    }

    public static Game createNewGame(GameConsole console) {
        ItemCatalog itemCatalog = ItemCatalog.createStandard();
        World world = new WorldFactory(itemCatalog).createWorld();

        GameState state = new GameState(world.getStartLocation());
        state.getInventory().add(itemCatalog.get(ItemId.SHOES));

        return new Game(console, itemCatalog, world, state, CommandRegistry.createStandard());
    }
}
