package cz.vse.escape.command;

import cz.vse.escape.core.Game;
import cz.vse.escape.interaction.ExitDoor;
import cz.vse.escape.interaction.Interactable;
import cz.vse.escape.text.Messages;

import java.util.List;

/** odejit – pokus o odchod z budovy hlavními dveřmi (funguje u dveří, s aktivovaným čipem). */
public final class LeaveBuildingCommand implements Command {

    @Override
    public String getName() {
        return "odejit";
    }

    @Override
    public String getUsage() {
        return "odejit";
    }

    @Override
    public String getDescription() {
        return "odchod z budovy hlavními dveřmi";
    }

    @Override
    public List<String> getAliases() {
        return List.of("odejdi", "ven");
    }

    @Override
    public void execute(String arguments, Game game) {
        for (Interactable interactable : game.getState().getCurrentLocation().getInteractables()) {
            if (interactable instanceof ExitDoor door) {
                door.tryToLeaveBuilding(game);
                return;
            }
        }
        game.say(Messages.NOT_AT_BUILDING_EXIT);
    }
}
