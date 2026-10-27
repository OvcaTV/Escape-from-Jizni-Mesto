package cz.vse.escape.text;

/**
 * Texty příběhu: úvod, konce hry a hlášky navázané na pravidla světa.
 * Dialogy postav jsou u příslušných postav / konverzací.
 */
public final class StoryTexts {

    public static final String INTRO = """
            Probudil ses v učebně JM357B. Při hodině jsi na chvilku zavřel oči, jenže z té chvilky se stal
            šlofík přes zavírací dobu. Škola je prázdná a potmě.
            Musíš se dostat ven z labyrintu budovy Jižního Města dřív, než dorazí studenti ranních cvičení.
            Pak už se přes hordy lidí ven nedostaneš. Profesoři ti to ale ulehčovat nebudou...
            Zadej 'napoveda' pro seznam příkazů.
            """;

    // --- Pravidla vstupu do místností ---
    public static final String MANDIK_BLOCKS_THE_WAY =
            "Ing. Mandík ti zablokoval cestu: „Kam si myslíte, že jdete? Nejdřív mi odevzdejte semestrální práci!“";
    public static final String JANITOR_BLOCKS_THE_WAY =
            "Školník ti blokuje cestu, chce vidět přezutí.";

    // --- Prohry ---
    public static final String TEACHERS_OFFICE_ENDING =
            "„Co tady děláte?!“ obořil se na tebe jeden z profesorů. „Za trest tu s námi hezky zůstanete až do rána!“";
    public static final String MANDIK_CATCHES_EMBEZZLER =
            "Sotva vkročíš do učebny, Ing. Mandík zvedne hlavu od oprav: „Vy jste mi vypil kávu za moje peníze?! "
                    + "To je zpronevěra peněžních prostředků!“ Chytí tě za rukáv a odvede přímo do kabinetu, "
                    + "kde tě sbor vyučujících zadrží až do rána.";
    public static final String JANITOR_DRAGS_YOU_AWAY =
            "Školník se zamračí, popadne tě za límec a bez dalších řečí tě odnese do kabinetu vyučujícího. "
                    + "Sbor profesorů tě tam za trest zadrží až do rána.";
    public static final String CLERKS_THROW_YOU_OUT =
            "Referentky si vyměnily významný pohled. „Takhle se tady s lidmi nemluví. Neslušně vychovaný student "
                    + "nám tu nemá co dělat.“ Než se naděješ, stojíš v kabinetu vyučujícího a sbor profesorů "
                    + "ti oznámí, že tu zůstaneš až do rána.";

    // --- Výhry ---
    public static final String WIN_WITH_CHIP =
            "Přiložil jsi aktivovaný čip ke čtečce. Zámek cvakne, dveře se otevřou a ty vyjdeš do chladného večera. "
                    + "Zvládl jsi to dřív, než dorazili studenti ranních cvičení, a spokojeně míříš domů.";
    public static final String WIN_WITH_CROWBAR =
            "Zapřel ses páčidlem do spáry mezi dveřmi a rámem. Zámek zapraskal a dveře povolily. "
                    + "Vyjdeš ven do chladného večera dřív, než dorazili studenti ranních cvičení, a spokojeně míříš domů.";
    public static final String WIN_BAREFOOT_NOTE =
            "(Bosé nohy tě studí na chodníku, ale svoboda stojí za to.)";

    public static final String BAREFOOT_NOTE = "Studená podlaha ti nepříjemně studí bosé nohy.";

    public static final String GAME_OVER_BANNER = "=== KONEC HRY – PROHRA ===";
    public static final String VICTORY_BANNER = "=== VÝHRA ===";

    private StoryTexts() {
    }
}
