package cz.vse.escape.item;

import java.util.EnumMap;
import java.util.Map;

/** Katalog všech předmětů hry. Nový předmět = nová hodnota v {@link ItemId} + řádek v {@link #createStandard()}. */
public final class ItemCatalog {

    private final Map<ItemId, Item> itemsById = new EnumMap<>(ItemId.class);

    public void register(Item item) {
        itemsById.put(item.id(), item);
    }

    public Item get(ItemId itemId) {
        Item item = itemsById.get(itemId);
        if (item == null) {
            throw new IllegalStateException("Předmět není v katalogu: " + itemId);
        }
        return item;
    }

    public static ItemCatalog createStandard() {
        ItemCatalog catalog = new ItemCatalog();
        catalog.register(Item.of(ItemId.USB_FLASH_DRIVE, "USB flashdisk",
                "USB flashdisk se semestrální prací pro Ing. Mandíka.",
                "flashdisk", "usb", "flash", "disk"));
        catalog.register(Item.of(ItemId.COFFEE_MONEY, "peníze na kávu",
                "Pár mincí, které ti Ing. Mandík svěřil na kávu z automatu.",
                "penize", "mince", "drobne"));
        catalog.register(Item.of(ItemId.COFFEE, "káva",
                "Horký kelímek kávy z automatu.",
                "kava", "kelimek"));
        catalog.register(Item.of(ItemId.CROWBAR, "páčidlo",
                "Těžké páčidlo. Dá se s ním omráčit školník nebo vypáčit zamčené dveře. Má jen jedno použití.",
                "pacidlo"));
        catalog.register(Item.of(ItemId.SHOES, "boty (přezutí)",
                "Tvoje boty. Školník na ně má neobyčejně dobrý zrak.",
                "boty", "prezuti", "bota"));
        catalog.register(Item.of(ItemId.INACTIVE_CHIP, "přístupový čip (neaktivní)",
                "Přístupový čip od Ing. Chlapka. Aby otevřel dveře, musí ho aktivovat studijní oddělení.",
                "cip", "pristupovy cip", "neaktivni cip"));
        catalog.register(Item.of(ItemId.ACTIVE_CHIP, "přístupový čip (aktivovaný)",
                "Aktivovaný přístupový čip. Otevře hlavní dveře z budovy.",
                "cip", "pristupovy cip", "aktivovany cip"));
        return catalog;
    }
}
