package cz.vse.escape.text;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Množina jmen (hlavní jméno + přezdívky), pod kterými hráč může na něco odkázat.
 * Porovnává se bez ohledu na diakritiku a velikost písmen.
 */
public final class NameSet {

    private final Set<String> normalizedNames;

    public NameSet(String primaryName, String... aliases) {
        Set<String> names = new LinkedHashSet<>();
        names.add(TextNormalizer.normalize(primaryName));
        for (String alias : aliases) {
            names.add(TextNormalizer.normalize(alias));
        }
        this.normalizedNames = Collections.unmodifiableSet(names);
    }

    public boolean matchesExactly(String userInput) {
        return normalizedNames.contains(TextNormalizer.normalize(userInput));
    }

    /** Vrací true, pokud některé jméno začíná zadaným textem (např. "chodba" -> "chodba do 2. části"). */
    public boolean matchesPrefix(String userInput) {
        String normalizedInput = TextNormalizer.normalize(userInput);
        if (normalizedInput.isEmpty()) {
            return false;
        }
        return normalizedNames.stream().anyMatch(name -> name.startsWith(normalizedInput));
    }
}
