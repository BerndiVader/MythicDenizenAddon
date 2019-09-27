package com.gmail.berndivader.mythicdenizenaddon.mechanics;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.denizenscript.denizencore.scripts.queues.core.TimedQueue;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DenizenScriptMechanic extends SkillMechanic implements INoTargetSkill, ITargetedLocationSkill, ITargetedEntitySkill {

    final String scriptName;
    HashMap<String, String> attributes;

    public DenizenScriptMechanic(String skill, MythicLineConfig mlc) {
        super(skill, mlc);
        this.ASYNC_SAFE = false;
        scriptName = config.getString("script", "");
        attributes = Utils.parseAttributes(skill);
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity abstractEntity) {
        return cast(data, Optional.ofNullable(abstractEntity), Optional.empty());
    }

    @Override
    public boolean castAtLocation(SkillMetadata data, AbstractLocation abstract_location) {
        return cast(data, Optional.empty(), Optional.ofNullable(abstract_location));
    }

    @Override
    public boolean cast(SkillMetadata data) {
        return cast(data, Optional.empty(), Optional.empty());
    }

    public boolean cast(SkillMetadata data, Optional<AbstractEntity> targetEntity, Optional<AbstractLocation> targetLocation) {
        ScriptTag script = new ScriptTag(scriptName);
        List<ScriptEntry> entries = null;

        if (script.isValid()) {
            try {
                ScriptEntry entry = new ScriptEntry(script.getName(), new String[0], script.getContainer());
                entry.setScript(scriptName);
                entries = script.getContainer().getBaseEntries(entry.entryData.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (entries == null) {
            return false;
        }

        ScriptQueue queue;
        String id = ScriptQueue.getNextId(script.getContainer().getName());
        long req_id = 0L;

        ScriptBuilder.addObjectToEntries(entries, "reqid", req_id);
        if (script.getContainer().contains("SPEED")) {
            long ticks = DurationTag.valueOf(script.getContainer().getString("SPEED", "0")).getTicks();
            queue = ticks > 0 ? ((TimedQueue) TimedQueue.getQueue(id).addEntries(entries)).setSpeed(ticks) : InstantQueue.getQueue(id).addEntries(entries);
        } else {
            queue = TimedQueue.getQueue(id).addEntries(entries);
        }

        HashMap<String, ObjectTag> context = new HashMap<>();
        context.put("data", new dMythicMeta(data));
        context.put("target", targetEntity.isPresent() ? new EntityTag(targetEntity.get().getBukkitEntity()) : targetLocation.isPresent() ? new LocationTag(BukkitAdapter.adapt(targetLocation.get())) : null);

        queue.setContextSource(new MythicContextSource(context));
        for (Map.Entry<String, String> item : attributes.entrySet()) {
            queue.addDefinition(item.getKey(), item.getValue());
        }
        queue.start();
        return true;
    }
}
