package cz.vse.escape.world;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Celý herní svět: všechny místnosti a startovní místnost. */
public final class World {

    private final Map<LocationId, Location> locations = new EnumMap<>(LocationId.class);
    private final LocationId startLocationId;

    public World(Collection<Location> locations, LocationId startLocationId) {
        for (Location location : locations) {
            this.locations.put(location.getId(), location);
        }
        this.startLocationId = startLocationId;
    }

    public Location getLocation(LocationId id) {
        Location location = locations.get(id);
        if (location == null) {
            throw new IllegalArgumentException("Místnost ve světě neexistuje: " + id);
        }
        return location;
    }

    public Location getStartLocation() {
        return getLocation(startLocationId);
    }

    public Collection<Location> getLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }
}
