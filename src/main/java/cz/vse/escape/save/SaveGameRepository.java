package cz.vse.escape.save;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFlag;
import cz.vse.escape.core.GameState;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.world.Location;
import cz.vse.escape.world.LocationId;
import cz.vse.escape.world.World;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Ukládá a načítá rozehranou hru do jednoduchého textového souboru (formát .properties).
 * <p>
 * Ukládá se: aktuální místnost, inventář, příznaky (stav questů a postav) a předměty ležící v každé místnosti.
 * Postavy se nikam nepřesouvají, takže stačí příznaky. Novou věc ve světě není nutné do ukládání nijak dopisovat.
 */
public final class SaveGameRepository {

    private static final String KEY_LOCATION = "location";
    private static final String KEY_INVENTORY = "inventory";
    private static final String KEY_FLAGS = "flags";
    private static final String KEY_PREFIX_LOCATION_ITEMS = "items.";
    private static final String LIST_SEPARATOR = ",";

    /** Načtený obsah souboru ještě před tím, než se použije na hru. */
    private record SavedGame(LocationId location, List<ItemId> inventory, List<GameFlag> flags,
                             Map<LocationId, List<ItemId>> itemsInLocations) {
    }

    // ------------------------------------------------------------------ ukládání

    public void save(Game game, Path file) throws SaveGameException {
        GameState state = game.getState();
        Properties properties = new Properties();

        properties.setProperty(KEY_LOCATION, state.getCurrentLocation().getId().name());
        properties.setProperty(KEY_INVENTORY, joinNames(state.getInventory().asList().stream()
                .map(item -> item.id().name()).toList()));
        properties.setProperty(KEY_FLAGS, joinNames(state.getFlags().stream().map(Enum::name).toList()));
        for (Location location : game.getWorld().getLocations()) {
            properties.setProperty(KEY_PREFIX_LOCATION_ITEMS + location.getId().name(),
                    joinNames(location.getItems().asList().stream().map(item -> item.id().name()).toList()));
        }

        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            properties.store(writer, "Uložená hra – Útěk z Jižního Města");
        } catch (IOException exception) {
            throw new SaveGameException("Hru se nepodařilo uložit: " + exception.getMessage(), exception);
        }
    }

    // ------------------------------------------------------------------ načítání

    public void load(Game game, Path file) throws SaveGameException {
        SavedGame savedGame = parse(readProperties(file));
        apply(savedGame, game);
    }

    private Properties readProperties(Path file) throws SaveGameException {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (NoSuchFileException exception) {
            throw new SaveGameException("Uložená hra nebyla nalezena: " + file);
        } catch (IllegalArgumentException exception) {
            // Properties.load takto hlásí poškozený obsah (např. chybný unicode zápis).
            throw new SaveGameException("Uložená hra je poškozená: " + exception.getMessage(), exception);
        } catch (IOException exception) {
            throw new SaveGameException("Hru se nepodařilo načíst: " + exception.getMessage(), exception);
        }
        return properties;
    }

    /** Celý soubor se nejdřív zkontroluje, aby poškozená uložená hra nezničila rozehranou. */
    private SavedGame parse(Properties properties) throws SaveGameException {
        String locationName = properties.getProperty(KEY_LOCATION);
        if (locationName == null) {
            throw new SaveGameException("Uložená hra je poškozená (chybí místnost).");
        }
        LocationId location = parseName(LocationId.class, locationName);
        List<ItemId> inventory = parseNames(ItemId.class, properties.getProperty(KEY_INVENTORY));
        List<GameFlag> flags = parseNames(GameFlag.class, properties.getProperty(KEY_FLAGS));

        Map<LocationId, List<ItemId>> itemsInLocations = new EnumMap<>(LocationId.class);
        for (LocationId locationId : LocationId.values()) {
            String savedItems = properties.getProperty(KEY_PREFIX_LOCATION_ITEMS + locationId.name());
            if (savedItems != null) {
                itemsInLocations.put(locationId, parseNames(ItemId.class, savedItems));
            }
        }
        return new SavedGame(location, inventory, flags, itemsInLocations);
    }

    private void apply(SavedGame savedGame, Game game) {
        World world = game.getWorld();
        GameState state = game.getState();

        state.setCurrentLocation(world.getLocation(savedGame.location()));
        state.getInventory().clear();
        savedGame.inventory().forEach(itemId -> state.getInventory().add(game.getItemCatalog().get(itemId)));
        state.replaceFlags(savedGame.flags());

        savedGame.itemsInLocations().forEach((locationId, itemIds) -> {
            Location location = world.getLocation(locationId);
            location.getItems().clear();
            itemIds.forEach(itemId -> location.getItems().add(game.getItemCatalog().get(itemId)));
        });
    }

    // ------------------------------------------------------------------ pomocné metody

    private String joinNames(List<String> names) {
        return names.stream().collect(Collectors.joining(LIST_SEPARATOR));
    }

    private <E extends Enum<E>> List<E> parseNames(Class<E> type, String joinedNames) throws SaveGameException {
        List<E> values = new ArrayList<>();
        if (joinedNames == null || joinedNames.isBlank()) {
            return values;
        }
        for (String name : joinedNames.split(LIST_SEPARATOR)) {
            values.add(parseName(type, name));
        }
        return values;
    }

    private <E extends Enum<E>> E parseName(Class<E> type, String name) throws SaveGameException {
        try {
            return Enum.valueOf(type, name.trim());
        } catch (IllegalArgumentException exception) {
            throw new SaveGameException("Uložená hra je poškozená (neznámá hodnota: " + name.trim() + ").");
        }
    }
}
