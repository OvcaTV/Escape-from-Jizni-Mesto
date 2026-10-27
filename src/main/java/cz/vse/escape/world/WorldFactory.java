package cz.vse.escape.world;

import cz.vse.escape.core.GameFlag;
import cz.vse.escape.dialog.JanitorEncounter;
import cz.vse.escape.interaction.Chlapek;
import cz.vse.escape.interaction.ExitDoor;
import cz.vse.escape.interaction.HanaJudeckova;
import cz.vse.escape.interaction.JanaHudeckova;
import cz.vse.escape.interaction.Janitor;
import cz.vse.escape.interaction.Mandik;
import cz.vse.escape.interaction.SleepingStudent;
import cz.vse.escape.interaction.VendingMachine;
import cz.vse.escape.item.ItemCatalog;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.text.StoryTexts;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static cz.vse.escape.world.LocationId.*;

/**
 * Staví herní svět podle Game Design Documentu.
 * <p>
 * Přidání nové místnosti: 1) nová hodnota v {@link LocationId}, 2) řádek v {@link #createLocations()},
 * 3) spojení v {@link #connectLocations()}, 4) volitelně postavy/předměty v {@link #populateLocations()}
 * a pravidla v {@link #addRules()}.
 */
public final class WorldFactory {

    private final ItemCatalog itemCatalog;
    private final Map<LocationId, Location> locations = new EnumMap<>(LocationId.class);

    public WorldFactory(ItemCatalog itemCatalog) {
        this.itemCatalog = itemCatalog;
    }

    public World createWorld() {
        locations.clear();
        createLocations();
        connectLocations();
        populateLocations();
        addRules();
        return new World(locations.values(), JM357B);
    }

    // ------------------------------------------------------------------ 1) místnosti

    private void createLocations() {
        define(JM357B, "Učebna JM357B",
                "Nacházíš se ve startovní místnosti – učebně, ve které jsi při hodině usnul. "
                        + "Lavice jsou prázdné, za okny je tma a v celé budově je ticho.");
        define(JM357A, "Učebna JM357A",
                "Propojená učebna. Na tabuli zůstal nedokončený příklad a u katedry se válí hromada "
                        + "neopravených semestrálních prací.");
        define(UPPER_CORRIDOR, "Chodba (3. patro)",
                "Stojíš na dlouhé chodbě 3. patra. Svítí tu jen nouzová světla a nic tu neleží.");
        define(UPPER_TOILET, "WC (3. patro)",
                "Vykachlíkované toalety, ve kterých to zvláštně voní po čističi. "
                        + "Někde kape kohoutek a odpočítává vteřiny.");
        define(TEACHERS_OFFICE, "Kabinet vyučujícího",
                "Vcházíš do kabinetu. Kolem stolu sedí sbor vyučujících a popíjejí kávu.");
        define(CORRIDOR_2, "Chodba 2 (2. patro)",
                "Úzká chodba druhého patra. U zdi stojí odřená pohovka a vedle ní bzučí automat na kávu.");
        define(JANITOR_CORRIDOR, "Chodba do 2. části (2. patro)",
                "Dlouhá chodba, která spojuje obě části budovy. Zářivky nad hlavou blikají.");
        define(CORRIDOR_3, "Chodba 3 (2. patro)",
                "Druhá část chodby ve 2. patře. Odtud vedou schody do přízemí a dveře na studijní oddělení.");
        define(STUDY_DEPARTMENT, "Studijní oddělení",
                "Kancelář plná šanonů a razítek. Za přepážkou sedí referentky.");
        define(CORRIDOR_4, "Chodba 4 (přízemí)",
                "Chodba v přízemí. Na jejím konci jsou hlavní dveře ven a vedle nich čtečka přístupových čipů.");
        define(JM104, "Přednášková místnost JM104",
                "Prostorná přednáškovna se stupňovitými lavicemi. Na plátně svítí nedokončený SQL dotaz.");
    }

    private void define(LocationId id, String name, String description) {
        locations.put(id, new Location(id, name, description));
    }

    // ------------------------------------------------------------------ 2) propojení

    private void connectLocations() {
        location(JM357B).connectTo(location(JM357A), "JM357A", "");

        location(JM357A).connectTo(location(JM357B), "JM357B", "");
        location(JM357A).connectTo(location(UPPER_CORRIDOR), "chodba", "");

        location(UPPER_CORRIDOR).connectTo(location(JM357A), "JM357A", "");
        location(UPPER_CORRIDOR).connectTo(location(UPPER_TOILET), "WC", "3. patro",
                "wc 3. patro", "zachody", "toalety");
        location(UPPER_CORRIDOR).connectTo(location(TEACHERS_OFFICE), "kabinet vyučujícího", "",
                "kabinet");
        location(UPPER_CORRIDOR).connectTo(location(CORRIDOR_2), "schodiště 2", "dolů do 2. patra");

        location(UPPER_TOILET).connectTo(location(UPPER_CORRIDOR), "chodba", "");

        location(CORRIDOR_2).connectTo(location(UPPER_CORRIDOR), "schodiště", "nahoru do 3. patra");
        location(CORRIDOR_2).connectTo(location(JANITOR_CORRIDOR), "chodba do 2. části", "");

        location(JANITOR_CORRIDOR).connectTo(location(CORRIDOR_2), "chodba 2", "");
        location(JANITOR_CORRIDOR).connectTo(location(CORRIDOR_3), "chodba 3", "");

        location(CORRIDOR_3).connectTo(location(JANITOR_CORRIDOR), "chodba do 2. části", "");
        location(CORRIDOR_3).connectTo(location(CORRIDOR_4), "schodiště 3", "dolů do 1. patra");
        location(CORRIDOR_3).connectTo(location(STUDY_DEPARTMENT), "studijní oddělení", "", "studijni");

        location(STUDY_DEPARTMENT).connectTo(location(CORRIDOR_3), "chodba 3", "");

        location(CORRIDOR_4).connectTo(location(CORRIDOR_3), "schodiště 4", "nahoru do 2. patra");
        location(CORRIDOR_4).connectTo(location(JM104), "JM104", "");

        location(JM104).connectTo(location(CORRIDOR_4), "chodba 4", "");
    }

    // ------------------------------------------------------------------ 3) předměty a postavy

    private void populateLocations() {
        location(JM357B).getItems().add(itemCatalog.get(ItemId.USB_FLASH_DRIVE));

        location(JM357A).addInteractable(new Mandik());
        location(CORRIDOR_2).addInteractable(new SleepingStudent());
        location(CORRIDOR_2).addInteractable(new VendingMachine());
        location(JANITOR_CORRIDOR).addInteractable(new Janitor());
        location(STUDY_DEPARTMENT).addInteractable(new JanaHudeckova());
        location(STUDY_DEPARTMENT).addInteractable(new HanaJudeckova());
        location(JM104).addInteractable(new Chlapek());
        location(CORRIDOR_4).addInteractable(new ExitDoor());
    }

    // ------------------------------------------------------------------ 4) pravidla vstupu

    private void addRules() {
        // Bez odevzdané práce Mandík nepustí hráče na chodbu.
        location(UPPER_CORRIDOR).addEntryGuard(state -> state.hasFlag(GameFlag.MANDIK_SATISFIED)
                ? Optional.empty()
                : Optional.of(StoryTexts.MANDIK_BLOCKS_THE_WAY));

        // Za školníka se bez jeho vyřešení nedá projít.
        location(CORRIDOR_3).addEntryGuard(state -> state.hasFlag(GameFlag.JANITOR_SOLVED)
                ? Optional.empty()
                : Optional.of(StoryTexts.JANITOR_BLOCKS_THE_WAY));

        // Kdo prodal kávu studentovi, tomu Mandík v učebně zatočí krkem.
        location(JM357A).addEntryEvent(game -> {
            if (game.getState().hasFlag(GameFlag.COFFEE_GIVEN_TO_STUDENT)) {
                game.lose(StoryTexts.MANDIK_CATCHES_EMBEZZLER);
            }
        });

        // Školník se vrhne na hráče hned po vstupu na jeho chodbu.
        location(JANITOR_CORRIDOR).addEntryEvent(game -> {
            if (!game.getState().hasFlag(GameFlag.JANITOR_SOLVED)) {
                game.showPortrait(Janitor.PORTRAIT_ID);
                game.startConversation(new JanitorEncounter());
            }
        });

        // Kabinet je past.
        location(TEACHERS_OFFICE).addEntryEvent(game -> game.lose(StoryTexts.TEACHERS_OFFICE_ENDING));
    }

    private Location location(LocationId id) {
        return locations.get(id);
    }
}
