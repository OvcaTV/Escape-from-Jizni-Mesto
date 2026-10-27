package cz.vse.escape;

import cz.vse.escape.core.GameConsole;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/** Testovací konzole: vstup bere ze skriptu a výstup si ukládá do přepisu. */
final class ScriptedConsole implements GameConsole {

    private final Deque<String> remainingInputs;
    private final StringBuilder transcript = new StringBuilder();

    ScriptedConsole(String... inputs) {
        this.remainingInputs = new ArrayDeque<>(Arrays.asList(inputs));
    }

    @Override
    public void println(String text) {
        transcript.append(text).append('\n');
    }

    @Override
    public String readLine(String prompt) {
        String input = remainingInputs.pollFirst();
        transcript.append(prompt).append(input == null ? "<konec vstupu>" : input).append('\n');
        return input;
    }

    String transcript() {
        return transcript.toString();
    }
}
