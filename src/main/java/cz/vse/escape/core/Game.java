package cz.vse.escape.core;

import cz.vse.escape.command.CommandRegistry;
import cz.vse.escape.dialog.Conversation;
import cz.vse.escape.item.ItemCatalog;
import cz.vse.escape.text.AsciiArt;
import cz.vse.escape.text.StoryTexts;
import cz.vse.escape.text.TextNormalizer;
import cz.vse.escape.world.Location;
import cz.vse.escape.world.World;

import java.util.Optional;

/**
 * Srdce hry: drží svět, stav hráče a rozhoduje, jestli se vstup zpracuje jako příkaz,
 * nebo jako odpověď v dialogu. Ostatní třídy (příkazy, postavy, dialogy) přes ni mění svět.
 */
public final class Game {

    private static final String INPUT_PROMPT = "> ";
    /** Příkaz, který jde zadat i uprostřed rozhovoru (jinak by se z dialogu nedalo odejít). */
    private static final String QUIT_COMMAND = "konec";

    private final GameConsole console;
    private final ItemCatalog itemCatalog;
    private final World world;
    private final GameState state;
    private final CommandRegistry commands;

    private Conversation activeConversation;
    private GameStatus status = GameStatus.RUNNING;

    public Game(GameConsole console, ItemCatalog itemCatalog, World world, GameState state,
                CommandRegistry commands) {
        this.console = console;
        this.itemCatalog = itemCatalog;
        this.world = world;
        this.state = state;
        this.commands = commands;
    }

    // ------------------------------------------------------------------ herní smyčka

    /** Spustí hru a vrátí se, až hra skončí (výhra, prohra, konec zadaný hráčem nebo konec vstupu). */
    public GameStatus run() {
        say(StoryTexts.INTRO);
        describeCurrentLocation();
        while (status == GameStatus.RUNNING) {
            String input = console.readLine(INPUT_PROMPT);
            if (input == null) {
                status = GameStatus.QUIT;
                break;
            }
            processInput(input);
        }
        return status;
    }

    public void processInput(String rawInput) {
        if (rawInput.isBlank()) {
            return;
        }
        if (activeConversation != null && !QUIT_COMMAND.equals(TextNormalizer.normalize(rawInput))) {
            activeConversation.handleInput(rawInput, this);
        } else {
            commands.execute(rawInput, this);
        }
    }

    // ------------------------------------------------------------------ pohyb

    /** Pokusí se přesunout hráče do cílové místnosti: ověří zákazy vstupu, popíše místnost a spustí události. */
    public void enter(Location destination) {
        Optional<String> blockReason = destination.findBlockReason(state);
        if (blockReason.isPresent()) {
            say(blockReason.get());
            return;
        }
        state.setCurrentLocation(destination);
        describeCurrentLocation();
        destination.runEntryEvents(this);
    }

    public void describeCurrentLocation() {
        say(state.getCurrentLocation().describe());
        if (state.hasFlag(GameFlag.BAREFOOT)) {
            say(StoryTexts.BAREFOOT_NOTE);
        }
    }

    // ------------------------------------------------------------------ dialogy

    /** Vypíše ASCII portrét postavy ze souboru ascii/<id>.txt (pokud existuje) a za ním prázdný řádek. */
    public void showPortrait(String portraitId) {
        AsciiArt.find(portraitId).ifPresent(art -> {
            say(art);
            say("");
        });
    }

    public void startConversation(Conversation conversation) {
        activeConversation = conversation;
        conversation.start(this);
    }

    public void endConversation() {
        activeConversation = null;
    }

    // ------------------------------------------------------------------ konec hry

    public void win(String text) {
        say(text);
        if (state.hasFlag(GameFlag.BAREFOOT)) {
            say(StoryTexts.WIN_BAREFOOT_NOTE);
        }
        say(StoryTexts.VICTORY_BANNER);
        finish(GameStatus.WON);
    }

    public void lose(String text) {
        say(text);
        say(StoryTexts.GAME_OVER_BANNER);
        finish(GameStatus.LOST);
    }

    public void quit() {
        finish(GameStatus.QUIT);
    }

    private void finish(GameStatus finalStatus) {
        activeConversation = null;
        status = finalStatus;
    }

    public boolean isRunning() {
        return status == GameStatus.RUNNING;
    }

    // ------------------------------------------------------------------ výstup a přístup ke stavu

    public void say(String text) {
        console.println(text);
    }

    public GameState getState() {
        return state;
    }

    public World getWorld() {
        return world;
    }

    public ItemCatalog getItemCatalog() {
        return itemCatalog;
    }

    public CommandRegistry getCommands() {
        return commands;
    }
}
