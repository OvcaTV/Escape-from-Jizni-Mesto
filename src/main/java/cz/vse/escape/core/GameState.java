package cz.vse.escape.core;

import cz.vse.escape.item.ItemCollection;
import cz.vse.escape.world.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** Proměnný stav hráče: kde je, co nese a co už se ve hře stalo. */
public final class GameState {

    private Location currentLocation;
    private final ItemCollection inventory = new ItemCollection();
    private final Set<GameFlag> flags = EnumSet.noneOf(GameFlag.class);

    public GameState(Location startLocation) {
        this.currentLocation = startLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public ItemCollection getInventory() {
        return inventory;
    }

    public boolean hasFlag(GameFlag flag) {
        return flags.contains(flag);
    }

    public void setFlag(GameFlag flag) {
        flags.add(flag);
    }

    public Set<GameFlag> getFlags() {
        return Collections.unmodifiableSet(flags);
    }

    public void replaceFlags(Collection<GameFlag> newFlags) {
        flags.clear();
        flags.addAll(newFlags);
    }
}
