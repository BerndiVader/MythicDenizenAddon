package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.entity.Player;

public class CustomObjectiveEvent extends AbstractEvent {

    public static enum Action {
        INCREMENT,
        COMPLETE,
        FAIL
    }

    private Player player;
    private Action action;
    private String objective_type;

    public CustomObjectiveEvent(Player player, Action action, String objective_type) {
        setPlayer(player);
        setAction(action);
        setObjectiveType(objective_type);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getObjectiveType() {
        return objective_type;
    }

    public void setObjectiveType(String objective_type) {
        this.objective_type = objective_type;
    }
}
