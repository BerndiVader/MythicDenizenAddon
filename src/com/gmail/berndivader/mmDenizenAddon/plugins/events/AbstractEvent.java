package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AbstractEvent
extends
Event {
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
