package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AbstractEvent extends Event {

    static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
