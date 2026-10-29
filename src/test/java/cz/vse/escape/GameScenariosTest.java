package cz.vse.escape;

import cz.vse.escape.core.Game;
import cz.vse.escape.core.GameFactory;
import cz.vse.escape.core.GameStatus;
import cz.vse.escape.item.ItemId;
import cz.vse.escape.world.LocationId;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Scénářové testy celé hry (JUnit 5, spouští se přes {@code mvn test}).
 * Každý scénář odehraje hru skriptovaným vstupem a zkontroluje výsledek a výpisy.
 */
final class GameScenariosTest {

    // Společný začátek: flashdisk -> Mandík -> chodba 3. patra -> chodba 2 (peníze v inventáři).
    private static final String[] START_TO_CORRIDOR_2 = {
            "seber flashdisk", "jdi jm357a", "pouzij flashdisk mandik", "jdi chodba", "jdi schodiste 2"
    };

    // ------------------------------------------------------------------ výhry

    @Test
    void winWithChipAndShoes() {
        Result result = play(join(START_TO_CORRIDOR_2,
                "pouzij penize automat",
                "jdi chodba do 2. casti", "B",            // sundat boty
                "jdi chodba 3", "jdi schodiste 3", "jdi jm104",
                "mluv chlapek", "A", "B",                   // špatně, pak správně
                "jdi chodba 4", "jdi schodiste 4", "jdi studijni oddeleni",
                "mluv referentky", "B", "B", "A", "C",      // 3 + 2 + 3 + 3 = 11 bodů (minimum je 9)
                "jdi chodba 3", "jdi schodiste 3",
                "odejit"));
        assertStatus(result, GameStatus.WON);
        assertContains(result, "Přiložil jsi aktivovaný čip");
        assertContains(result, "Studená podlaha");
        assertContains(result, "Bosé nohy");
        assertContains(result, "Čip je aktivovaný");
    }

    @Test
    void winWithCrowbarThroughDoor() {
        Result result = play(join(START_TO_CORRIDOR_2,
                "pouzij penize automat",
                "pouzij kava student",
                "jdi schodiste", "jdi wc", "seber pacidlo", "jdi chodba", "jdi schodiste 2",
                "jdi chodba do 2. casti", "B",              // boty dolů, páčidlo zůstane
                "jdi chodba 3", "jdi schodiste 3",
                "odejit",                                   // zamčeno
                "pouzij pacidlo dvere"));
        assertStatus(result, GameStatus.WON);
        assertContains(result, "Dveře jsou zamčené");
        assertContains(result, "Zapřel ses páčidlem");
    }

    @Test
    void winWithoutDiacriticsAndWithPartialNames() {
        Result result = play(join(START_TO_CORRIDOR_2,
                "POUZIJ Peníze",                            // velká písmena, diakritika, cíl se dovodí
                "jdi chodba",                               // jednoznačný začátek jména východu
                "C"));                                      // ignorovat školníka -> prohra
        assertStatus(result, GameStatus.LOST);
        assertContains(result, "Vhodil jsi mince do automatu");
    }

    // ------------------------------------------------------------------ prohry

    @Test
    void loseInTeachersOffice() {
        Result result = play("seber flashdisk", "jdi jm357a", "pouzij flashdisk mandik", "jdi chodba",
                "jdi kabinet vyucujiciho");
        assertStatus(result, GameStatus.LOST);
        assertContains(result, "Za trest tu s námi hezky zůstanete až do rána!");
        assertContains(result, "KONEC HRY");
    }

    @Test
    void loseByIgnoringJanitor() {
        Result result = play(join(START_TO_CORRIDOR_2, "jdi chodba do 2. casti", "C"));
        assertStatus(result, GameStatus.LOST);
        assertContains(result, "odnese do kabinetu");
    }

    @Test
    void loseByRudenessToClerks() {
        Result result = play(join(START_TO_CORRIDOR_2,
                "jdi chodba do 2. casti", "B", "jdi chodba 3", "jdi schodiste 3", "jdi jm104",
                "mluv chlapek", "B", "jdi chodba 4", "jdi schodiste 4", "jdi studijni oddeleni",
                "mluv referentky", "A", "A", "B", "A"));    // samé nízké body
        assertStatus(result, GameStatus.LOST);
        assertContains(result, "Neslušně vychovaný student");
    }

    @Test
    void loseByEnteringMandikAfterGivingCoffeeToStudent() {
        Result result = play(join(START_TO_CORRIDOR_2,
                "pouzij penize automat", "pouzij kava student",
                "jdi schodiste", "jdi jm357a"));
        assertStatus(result, GameStatus.LOST);
        assertContains(result, "zpronevěra");
    }

    // ------------------------------------------------------------------ pravidla

    @Test
    void crowbarOnJanitorKeepsShoesAndLosesCrowbar() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "pouzij penize automat", "pouzij kava student",
                "jdi schodiste", "jdi wc", "seber pacidlo", "jdi chodba", "jdi schodiste 2",
                "jdi chodba do 2. casti", "A",              // omráčit páčidlem
                "inventar"));
        assertContains(result.transcript, "Rázně jsi školníka praštil páčidlem");
        assertContains(result.transcript, "U sebe máš: boty (přezutí).");
        check(result.game.getState().getInventory().contains(ItemId.SHOES), "boty zůstaly");
        check(!result.game.getState().getInventory().contains(ItemId.CROWBAR), "páčidlo zmizelo");
    }

    @Test
    void errorMessages() {
        Result result = play("tancuj", "jdi nikam", "mluv mandik", "pouzij pacidlo", "seber nic",
                "pouzij flashdisk", "jdi jm357a", "mluv hana", "jdi chodba", "inventar", "napoveda");
        assertContains(result, "Neznámý příkaz. Zkus zadat 'napoveda'.");
        assertContains(result, "Tímto směrem cesta nevede.");
        assertContains(result, "Tady nikdo takový není.");
        assertContains(result, "Tento předmět u sebe nemáš.");
        assertContains(result, "Nic takového tu není.");
        assertContains(result, "Ing. Mandík ti zablokoval cestu");
        assertContains(result, "Použitelné příkazy:");
        assertContains(result, "U sebe máš: boty (přezutí).");
    }

    @Test
    void mandikBlocksUntilProjectIsHandedIn() {
        Result result = playKeepingGame("jdi jm357a", "jdi chodba");
        check(result.game.getState().getCurrentLocation().getId() == LocationId.JM357A,
                "hráč zůstal u Mandíka");
        assertContains(result.transcript, "Nejdřív mi odevzdejte semestrální práci!");
    }

    // ------------------------------------------------------------------ ASCII portréty

    @Test
    void portraitsAreShownWhenTalking() throws IOException {
        // Mandík (první rozhovor) i školník (automatické setkání po vstupu na chodbu) mají svůj obrázek.
        Result mandik = playKeepingGame("jdi jm357a", "mluv mandik");
        assertContains(mandik.transcript, firstLineOf("mandik"));
        Result janitor = playKeepingGame(join(START_TO_CORRIDOR_2,
                "pouzij penize automat", "jdi chodba do 2. casti"));
        assertContains(janitor.transcript, firstLineOf("skolnik"));
        // Všechny postavy mají obrázek v samostatném souboru.
        for (String id : new String[] {"mandik", "chlapek", "jana", "hana", "skolnik", "student"}) {
            check(cz.vse.escape.text.AsciiArt.find(id).isPresent(), "existuje ascii/" + id + ".txt");
        }
    }

    @Test
    void missingPortraitFileIsIgnored() {
        check(cz.vse.escape.text.AsciiArt.find("neexistuje").isEmpty(), "chybějící soubor nevadí");
        check(cz.vse.escape.text.AsciiArt.find("../README").isEmpty(), "cesta mimo složku se odmítne");
    }

    private static String firstLineOf(String id) throws IOException {
        return Files.readAllLines(Path.of("ascii", id + ".txt")).get(0);
    }

    @Test
    void coffeeToMandikGivesNoCrowbar() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "pouzij penize automat",
                "jdi schodiste", "jdi jm357a", "pouzij kava mandik",
                "jdi chodba", "jdi wc", "seber pacidlo"));
        assertContains(result.transcript, "Žádnou odměnu");
        check(result.game.getState().getInventory().asList().stream()
                        .noneMatch(item -> item.id() == ItemId.CROWBAR),
                "páčidlo se neobjevilo");
    }

    // ------------------------------------------------------------------ ukládání

    @Test
    void saveAndLoad() throws IOException {
        String fileName = "test-save.properties";
        Path file = Path.of(fileName);
        try {
            playKeepingGame(join(START_TO_CORRIDOR_2,
                    "pouzij penize automat", "pouzij kava student",
                    "uloz " + fileName));

            ScriptedConsole loadingConsole = new ScriptedConsole("nacti " + fileName, "jdi schodiste",
                    "jdi wc", "seber pacidlo", "inventar");
            Game loadedGame = GameFactory.createNewGame(loadingConsole);
            loadedGame.run();

            String transcript = loadingConsole.transcript();
            assertContains(transcript, "Hra byla načtena");
            assertContains(transcript, "== Chodba 2 (2. patro) ==");
            assertContains(transcript, "Sebral jsi: páčidlo.");
            // Mandík už po načtení nesmí hráče pustit zpět: příznak kávy pro studenta se uložil.
            ScriptedConsole mandikConsole = new ScriptedConsole("nacti " + fileName, "jdi schodiste",
                    "jdi jm357a");
            Game mandikGame = GameFactory.createNewGame(mandikConsole);
            check(mandikGame.run() == GameStatus.LOST, "po načtení platí zákaz vstupu k Mandíkovi");

            ScriptedConsole missingConsole = new ScriptedConsole("nacti neexistuje.properties");
            GameFactory.createNewGame(missingConsole).run();
            assertContains(missingConsole.transcript(), "Uložená hra nebyla nalezena");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    // ------------------------------------------------------------------ odolnost vůči chybám

    @Test
    void corruptedSaveFileDoesNotCrashTheGame() throws IOException {
        Path file = Path.of("test-save-corrupted.properties");
        try {
            Files.writeString(file, "location=JM357A\\uZZ\n");
            Result result = playKeepingGame("nacti " + file);
            assertContains(result.transcript, "Uložená hra je poškozená");
            check(result.game.getState().getCurrentLocation().getId() == LocationId.JM357B,
                    "rozehraná hra zůstala nedotčená");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void unknownValueInSaveFileIsRejected() throws IOException {
        Path file = Path.of("test-save-unknown.properties");
        try {
            Files.writeString(file, "location=NEEXISTUJE\n");
            assertContains(playKeepingGame("nacti " + file), "Uložená hra je poškozená");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void quitWorksInsideConversation() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "jdi chodba do 2. casti", "konec"));
        assertStatus(result, GameStatus.QUIT);
    }

    @Test
    void onlyLettersAreAcceptedInConversation() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "jdi chodba do 2. casti", "blabla", "D", "B"));
        assertContains(result.transcript, "Odpověz prosím písmenem A až C.");
        check(result.game.getState().hasFlag(cz.vse.escape.core.GameFlag.JANITOR_SOLVED), "školník vyřešen");
    }

    @Test
    void crowbarChoiceWithoutCrowbarAsksAgain() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "jdi chodba do 2. casti", "A", "B"));
        assertContains(result.transcript, "Páčidlo u sebe nemáš");
        check(result.game.getState().hasFlag(cz.vse.escape.core.GameFlag.BAREFOOT), "nakonec bos");
    }

    @Test
    void sqlQuizRepeatsUntilCorrectAnswer() {
        Result result = playKeepingGame(join(START_TO_CORRIDOR_2,
                "jdi chodba do 2. casti", "B", "jdi chodba 3", "jdi schodiste 3", "jdi jm104",
                "mluv chlapek", "A", "C", "B"));
        check(result.game.getState().getInventory().contains(ItemId.INACTIVE_CHIP), "čip získán");
    }

    @Test
    void textNormalizerRemovesDiacriticsCaseAndSpaces() {
        org.junit.jupiter.api.Assertions.assertEquals("studijni oddeleni",
                cz.vse.escape.text.TextNormalizer.normalize("  Studijní   ODDĚLENÍ "));
    }

    // ------------------------------------------------------------------ pomocné metody

    private record Result(Game game, GameStatus status, String transcript) {
    }

    private static Result playKeepingGame(String... inputs) {
        ScriptedConsole console = new ScriptedConsole(inputs);
        Game game = GameFactory.createNewGame(console);
        GameStatus status = game.run();
        return new Result(game, status, console.transcript());
    }

    private static Result play(String... inputs) {
        return playKeepingGame(inputs);
    }

    private static String[] join(String[] first, String... rest) {
        String[] combined = new String[first.length + rest.length];
        System.arraycopy(first, 0, combined, 0, first.length);
        System.arraycopy(rest, 0, combined, first.length, rest.length);
        return combined;
    }

    private static void assertStatus(Result result, GameStatus expected) {
        check(result.status == expected,
                "očekáván stav " + expected + ", ale hra skončila stavem " + result.status
                        + "\n--- přepis ---\n" + result.transcript);
    }

    private static void assertContains(Result result, String expectedText) {
        assertContains(result.transcript, expectedText);
    }

    private static void assertContains(String transcript, String expectedText) {
        check(transcript.contains(expectedText),
                "ve výpisu chybí text: " + expectedText + "\n--- přepis ---\n" + transcript);
    }

    private static void check(boolean condition, String failureMessage) {
        assertTrue(condition, failureMessage);
    }
}
