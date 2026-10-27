package cz.vse.escape.command;

import cz.vse.escape.core.Game;

import java.util.List;

/** Příkaz, který hráč zadává v běžném režimu (mimo dialog). */
public interface Command {

    /** Hlavní jméno příkazu, např. "jdi". */
    String getName();

    /** Návod k použití pro nápovědu, např. "jdi [lokace]". */
    String getUsage();

    String getDescription();

    /** Další jména, pod kterými jde příkaz zadat (v nápovědě se neuvádějí). */
    default List<String> getAliases() {
        return List.of();
    }

    /** @param arguments zbytek řádku za jménem příkazu, už bez diakritiky a malými písmeny */
    void execute(String arguments, Game game);
}
