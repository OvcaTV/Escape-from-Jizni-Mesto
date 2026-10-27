package cz.vse.escape.world;

import cz.vse.escape.text.NameSet;

/** Jednosměrná cesta z jedné místnosti do druhé. Hráč ji vybírá příkazem "jdi [název]". */
public final class Exit {

    private final Location target;
    private final String commandName;
    private final String hint;
    private final NameSet names;

    /**
     * @param commandName jak se východ jmenuje ve výpisu a co hráč píše za "jdi"
     * @param hint        doplněk ve výpisu v závorce (může být prázdný), např. "dolů do 2. patra"
     * @param aliases     další přijímaná jména východu
     */
    public Exit(Location target, String commandName, String hint, String... aliases) {
        this.target = target;
        this.commandName = commandName;
        this.hint = hint;
        this.names = new NameSet(commandName, aliases);
    }

    public Location getTarget() {
        return target;
    }

    public boolean isCalled(String userInput) {
        return names.matchesExactly(userInput);
    }

    public boolean isCalledByPrefix(String userInput) {
        return names.matchesPrefix(userInput);
    }

    public String getLabel() {
        return hint.isBlank() ? commandName : commandName + " (" + hint + ")";
    }
}
