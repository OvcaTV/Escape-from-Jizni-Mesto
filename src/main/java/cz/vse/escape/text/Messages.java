package cz.vse.escape.text;

/**
 * Obecné hlášky hry, které nepatří k žádné konkrétní postavě ani místnosti.
 * Texty příběhu jsou v {@link StoryTexts}.
 */
public final class Messages {

    public static final String UNKNOWN_COMMAND = "Neznámý příkaz. Zkus zadat 'napoveda'.";
    public static final String NOBODY_LIKE_THAT_HERE = "Tady nikdo takový není.";
    public static final String CANNOT_DO_THAT_NOW = "Tohle tady teď nejde udělat.";
    public static final String ITEM_NOT_OWNED = "Tento předmět u sebe nemáš.";
    public static final String NO_WAY_THERE = "Tímto směrem cesta nevede.";
    public static final String AMBIGUOUS_DESTINATION = "Upřesni prosím, kam přesně chceš jít.";
    public static final String NOTHING_TO_TAKE = "Nic takového tu není.";
    public static final String NOTHING_VISIBLE = "Nic takového tu nevidíš.";
    public static final String ITEM_HAS_NO_EFFECT = "Tohle nemá žádný účinek.";
    public static final String EMPTY_INVENTORY = "U sebe nemáš nic.";
    public static final String AMBIGUOUS_TARGET =
            "Na co přesně chceš předmět použít? Zadej: pouzij [předmět] [cíl]";

    public static final String ASK_WHERE_TO_GO = "Kam chceš jít? Zadej: jdi [lokace]";
    public static final String ASK_WHAT_TO_TAKE = "Co chceš sebrat? Zadej: seber [předmět]";
    public static final String ASK_WHAT_TO_USE = "Co chceš použít? Zadej: pouzij [předmět] [cíl]";
    public static final String ASK_WHO_TO_TALK_TO = "S kým chceš mluvit? Zadej: mluv [postava]";

    public static final String NOT_AT_BUILDING_EXIT = "Tady žádný východ z budovy není.";
    public static final String GAME_QUIT = "Hra ukončena.";

    private Messages() {
    }
}
