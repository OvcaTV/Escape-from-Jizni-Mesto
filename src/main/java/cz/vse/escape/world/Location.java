package cz.vse.escape.world;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameState;
import cz.vse.escape.interaction.Interactable;
import cz.vse.escape.interaction.Npc;
import cz.vse.escape.interaction.Prop;
import cz.vse.escape.item.ItemCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Jedna místnost (nebo chodba) herního světa: východy, ležící věci, postavy, zákazy vstupu a události. */
public final class Location {

    private final LocationId id;
    private final String name;
    private final String description;

    private final List<Exit> exits = new ArrayList<>();
    private final ItemCollection items = new ItemCollection();
    private final List<Interactable> interactables = new ArrayList<>();
    private final List<EntryGuard> entryGuards = new ArrayList<>();
    private final List<EntryEvent> entryEvents = new ArrayList<>();

    public Location(LocationId id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public LocationId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemCollection getItems() {
        return items;
    }

    public List<Interactable> getInteractables() {
        return Collections.unmodifiableList(interactables);
    }

    // ------------------------------------------------------------------ stavba místnosti

    public void connectTo(Location target, String commandName, String hint, String... aliases) {
        exits.add(new Exit(target, commandName, hint, aliases));
    }

    public void addInteractable(Interactable interactable) {
        interactables.add(interactable);
    }

    public void addEntryGuard(EntryGuard guard) {
        entryGuards.add(guard);
    }

    public void addEntryEvent(EntryEvent event) {
        entryEvents.add(event);
    }

    // ------------------------------------------------------------------ dotazy

    /** Najde východy odpovídající zadání: nejdřív přesná shoda, jinak shoda podle začátku jména. */
    public List<Exit> findExits(String userInput) {
        List<Exit> exactMatches = exits.stream().filter(exit -> exit.isCalled(userInput)).toList();
        if (!exactMatches.isEmpty()) {
            return exactMatches;
        }
        return exits.stream().filter(exit -> exit.isCalledByPrefix(userInput)).toList();
    }

    public Optional<Interactable> findInteractable(String userInput) {
        Optional<Interactable> exactMatch = interactables.stream()
                .filter(interactable -> interactable.isCalled(userInput))
                .findFirst();
        if (exactMatch.isPresent()) {
            return exactMatch;
        }
        return interactables.stream()
                .filter(interactable -> interactable.isCalledByPrefix(userInput))
                .findFirst();
    }

    public Optional<String> findBlockReason(GameState state) {
        for (EntryGuard guard : entryGuards) {
            Optional<String> reason = guard.blockReason(state);
            if (reason.isPresent()) {
                return reason;
            }
        }
        return Optional.empty();
    }

    public void runEntryEvents(Game game) {
        for (EntryEvent event : entryEvents) {
            if (!game.isRunning()) {
                return;
            }
            event.onEntered(game);
        }
    }

    // ------------------------------------------------------------------ popis

    public String describe() {
        StringBuilder text = new StringBuilder();
        text.append("== ").append(name).append(" ==\n").append(description);

        if (!items.isEmpty()) {
            text.append("\nVidíš zde: ").append(items.describe()).append('.');
        }
        appendInteractables(text, Npc.class, "Stojí zde: ");
        appendInteractables(text, Prop.class, "Najdeš tu také: ");

        if (exits.isEmpty()) {
            text.append("\nOdsud nevede žádná cesta.");
        } else {
            String exitLabels = exits.stream().map(Exit::getLabel).collect(Collectors.joining(", "));
            text.append("\nVýchody: ").append(exitLabels).append('.');
        }
        return text.toString();
    }

    private void appendInteractables(StringBuilder text, Class<? extends Interactable> kind, String prefix) {
        List<String> names = interactables.stream()
                .filter(kind::isInstance)
                .map(Interactable::getDisplayName)
                .toList();
        if (!names.isEmpty()) {
            text.append('\n').append(prefix).append(String.join(", ", names)).append('.');
        }
    }
}
