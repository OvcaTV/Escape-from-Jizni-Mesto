package cz.vse.escape.dialog;

import java.util.List;

/** Otázka postavy a nabídka odpovědí A, B, C. */
public record ChoiceQuestion(String prompt, List<ChoiceOption> options) {

    public List<String> optionTexts() {
        return options.stream().map(ChoiceOption::text).toList();
    }
}
