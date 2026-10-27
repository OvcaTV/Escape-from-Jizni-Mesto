package cz.vse.escape.item;

import cz.vse.escape.text.NameSet;

/** Neměnný popis předmětu. Kde se předmět zrovna nachází, určuje {@link ItemCollection}. */
public record Item(ItemId id, String displayName, String description, NameSet names) {

    public static Item of(ItemId id, String displayName, String description, String... aliases) {
        return new Item(id, displayName, description, new NameSet(displayName, aliases));
    }

    public boolean isCalled(String userInput) {
        return names.matchesExactly(userInput);
    }
}
