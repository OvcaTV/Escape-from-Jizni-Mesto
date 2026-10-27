package cz.vse.escape.text;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Převádí text zadaný hráčem do jednotného tvaru (malá písmena, bez diakritiky,
 * bez nadbytečných mezer), aby hra rozuměla "Peníze", "penize" i "  PENIZE ".
 */
public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static String normalize(String rawText) {
        String withoutDiacritics = Normalizer.normalize(rawText, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return withoutDiacritics
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
    }
}
