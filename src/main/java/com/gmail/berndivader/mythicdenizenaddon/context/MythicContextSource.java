package com.gmail.berndivader.mythicdenizenaddon.context;

import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.queues.ContextSource;

import java.util.HashMap;

public class MythicContextSource implements ContextSource {

    HashMap<String, ObjectTag> context;

    public MythicContextSource(HashMap<String, ObjectTag> context) {
        this.context = new HashMap<>();
        this.context.putAll(context);
    }

    @Override
    public ObjectTag getContext(String id) {
        id = id.toLowerCase();
        if (this.context.containsKey(id.toLowerCase())) {
            return this.context.get(id);
        }
        return null;
    }

    @Override
    public boolean getShouldCache() {
        return false;
    }

    public HashMap<String, ObjectTag> getContexts() {
        return this.context;
    }
}
