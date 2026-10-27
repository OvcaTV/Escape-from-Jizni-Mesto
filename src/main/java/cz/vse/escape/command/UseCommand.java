package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.interaction.Interactable;
import cz.vse.escape.item.Item;
import cz.vse.escape.text.Messages;

import java.util.List;
import java.util.Optional;

/**
 * pouzij [předmět] [cíl] – použije předmět z inventáře na postavu nebo věc v místnosti.
 * Pokud hráč cíl nezadá a v místnosti je právě jeden vhodný, použije se rovnou na něj.
 */
public final class UseCommand implements Command {

    @Override
    public String getName() {
        return "pouzij";
    }

    @Override
    public String getUsage() {
        return "pouzij [předmět] [cíl]";
    }

    @Override
    public String getDescription() {
        return "použití předmětu na postavu či věc (např. pouzij penize automat, pouzij pacidlo)";
    }

    @Override
    public void execute(String arguments, Game game) {
        if (arguments.isBlank()) {
            game.say(Messages.ASK_WHAT_TO_USE);
            return;
        }
        List<String> words = List.of(arguments.split(" "));

        // Jméno předmětu může mít víc slov, proto zkoušíme od nejdelšího začátku zadání po nejkratší.
        for (int itemWordCount = words.size(); itemWordCount >= 1; itemWordCount--) {
            String itemName = String.join(" ", words.subList(0, itemWordCount));
            Optional<Item> item = game.getState().getInventory().findByName(itemName);
            if (item.isPresent()) {
                String targetName = String.join(" ", words.subList(itemWordCount, words.size()));
                useItemOnTarget(item.get(), targetName, game);
                return;
            }
        }
        game.say(Messages.ITEM_NOT_OWNED);
    }

    private void useItemOnTarget(Item item, String targetName, Game game) {
        if (targetName.isBlank()) {
            useItemOnOnlySuitableTarget(item, game);
            return;
        }
        Optional<Interactable> target = game.getState().getCurrentLocation().findInteractable(targetName);
        if (target.isEmpty()) {
            game.say(Messages.CANNOT_DO_THAT_NOW);
        } else if (target.get().acceptsItem(item, game)) {
            target.get().useItem(item, game);
        } else {
            game.say(Messages.ITEM_HAS_NO_EFFECT);
        }
    }

    private void useItemOnOnlySuitableTarget(Item item, Game game) {
        List<Interactable> suitableTargets = game.getState().getCurrentLocation().getInteractables().stream()
                .filter(interactable -> interactable.acceptsItem(item, game))
                .toList();
        if (suitableTargets.isEmpty()) {
            game.say(Messages.ITEM_HAS_NO_EFFECT);
        } else if (suitableTargets.size() == 1) {
            suitableTargets.get(0).useItem(item, game);
        } else {
            game.say(Messages.AMBIGUOUS_TARGET);
        }
    }
}
