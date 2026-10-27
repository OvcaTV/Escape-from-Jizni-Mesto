package cz.vse.escape.text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Načítá ASCII grafiku postav. Každý obrázek je v samostatném souboru {@code ascii/<id>.txt}.
 *
 * <p>Hledá se nejdřív ve složce {@code ascii} vedle hry (pracovní adresář, dá se přepsat vlastností
 * {@code escape.ascii.dir}), potom v classpath ({@code /ascii/<id>.txt}, např. uvnitř jar).
 * Chybějící soubor hru nikdy nerozbije – obrázek se prostě nevypíše.
 */
public final class AsciiArt {

    private static final String DIRECTORY_PROPERTY = "escape.ascii.dir";
    private static final String DEFAULT_DIRECTORY = "ascii";
    private static final String EXTENSION = ".txt";

    private AsciiArt() {
    }

    /** Vrátí obrázek s daným id (např. "mandik"), nebo prázdný výsledek, pokud soubor neexistuje. */
    public static Optional<String> find(String id) {
        if (id == null || !id.matches("[a-z0-9_-]+")) {
            return Optional.empty();
        }
        String fileName = id + EXTENSION;
        Optional<String> fromDisk = readFromDisk(fileName);
        return fromDisk.isPresent() ? fromDisk : readFromClasspath(fileName);
    }

    private static Optional<String> readFromDisk(String fileName) {
        Path path = Path.of(System.getProperty(DIRECTORY_PROPERTY, DEFAULT_DIRECTORY), fileName);
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        try {
            return Optional.of(clean(Files.readString(path, StandardCharsets.UTF_8)));
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    private static Optional<String> readFromClasspath(String fileName) {
        try (InputStream stream = AsciiArt.class.getResourceAsStream("/" + DEFAULT_DIRECTORY + "/" + fileName)) {
            if (stream == null) {
                return Optional.empty();
            }
            return Optional.of(clean(new String(stream.readAllBytes(), StandardCharsets.UTF_8)));
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    /** Sjednotí konce řádků a odstraní prázdné řádky na konci (ne odsazení zleva). */
    private static String clean(String raw) {
        return raw.replace("\r\n", "\n").replaceAll("\\s+$", "");
    }
}
