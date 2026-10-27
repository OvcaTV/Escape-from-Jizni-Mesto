package cz.vse.escape.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/** Klasická konzole (System.in / System.out) v UTF-8. */
public final class StandardGameConsole implements GameConsole {

    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private final PrintStream output = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    @Override
    public void println(String text) {
        output.println(text);
    }

    @Override
    public String readLine(String prompt) {
        output.print(prompt);
        output.flush();
        try {
            return reader.readLine();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
