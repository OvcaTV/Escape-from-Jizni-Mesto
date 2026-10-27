package cz.vse.escape.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Seznam předmětů – používá se pro inventář hráče i pro věci ležící v místnostech. */
public final class ItemCollection {

    private final List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
    }

    public boolean remove(ItemId itemId) {
        return items.removeIf(item -> item.id() == itemId);
    }

    public boolean contains(ItemId itemId) {
        return items.stream().anyMatch(item -> item.id() == itemId);
    }

    public Optional<Item> findByName(String userInput) {
        return items.stream().filter(item -> item.isCalled(userInput)).findFirst();
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Item> asList() {
        return Collections.unmodifiableList(items);
    }

    /** Např. "boty (přezutí), peníze na kávu". */
    public String describe() {
        return items.stream().map(Item::displayName).collect(Collectors.joining(", "));
    }
}
