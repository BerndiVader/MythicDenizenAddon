package com.gmail.berndivader.mythicdenizenaddon;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

import java.util.*;

public class Utils {

    public static <E extends Enum<E>> E enumLookup(Class<E> enumClass, String id) {
        E result = null;
        try {
            result = Enum.valueOf(enumClass, id);
        } catch (IllegalArgumentException e) {
            // Do nothing
        }
        return result;
    }

    public static AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>> splitTargetList(ListTag list) {
        HashSet<AbstractLocation> locTargets = new HashSet<>();
        HashSet<AbstractEntity> entityTargets = new HashSet<>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            ElementTag element = (ElementTag) list.getObject(i);
            if (element.matchesType(LocationTag.class)) {
                locTargets.add(BukkitAdapter.adapt(element.asType(LocationTag.class)));
            } else if (element.matchesType(EntityTag.class)) {
                entityTargets.add(BukkitAdapter.adapt(element.asType(EntityTag.class).getBukkitEntity()));
            }
        }
        return new AbstractMap.SimpleEntry<>(entityTargets, locTargets);
    }

    public static HashMap<String, String> parseAttributes(String line) {
        if (!line.contains("{") || !line.contains("}")) {
            return null;
        }
        String[] parse = line.split("\\{")[1].split("}")[0].split(";");
        HashMap<String, String> attributes = new HashMap<>();

        int size = parse.length;
        for (int i = 0; i < size; i++) {
            if (parse[i].startsWith("script")) {
                continue;
            }
            String[] arr1 = parse[i].split("=");
            if (arr1.length == 2) {
                attributes.put(arr1[0], arr1[1]);
            }
        }
        return attributes;
    }

    public static ListTag getTargetsForScriptTargeter(SkillMetadata data, String scriptName, HashMap<String, String> attributes) {
        ScriptEntry entry = null;
        List<ScriptEntry> entries = null;
        ScriptTag script = new ScriptTag(scriptName);
        if (script.isValid()) {
            try {
                entry = new ScriptEntry(script.getName(), new String[0], script.getContainer());
                entry.setScript(scriptName);
                entries = script.getContainer().getBaseEntries(entry.entryData.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (entries != null) {
            String id = ScriptQueue.getNextId(script.getContainer().getName());
            long reqId = 0L;
            ScriptBuilder.addObjectToEntries(entries, "reqid", reqId);

            HashMap<String, ObjectTag> context = new HashMap<>();
            context.put("data", new dMythicMeta(data));

            ScriptQueue queue = InstantQueue.getQueue(id).addEntries(entries);
            queue.setContextSource(new MythicContextSource(context));
            for (Map.Entry<String, String> item : attributes.entrySet()) {
                queue.addDefinition(item.getKey(), item.getValue());
            }

            final ScriptEntry final_entry = entry;
            queue.callBack(() -> final_entry.setFinished(true));
            queue.start();

            while (!final_entry.isFinished) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<String> queueArgs = queue.getLastEntryExecuted().getArguments();
            for (String s1 : queueArgs) {
                Argument arg = new Argument(s1);
                if (arg.matchesArgumentType(ListTag.class)) {
                    return arg.getList();
                }
            }

        }
        return null;
    }

    /**
     * @param targeter {@link String}
     * @return skill_targeter {@link SkillTargeter}
     */
    public static SkillTargeter parseSkillTargeter(String targeter) {
        String search = targeter.substring(1);
        MythicLineConfig mlc = new MythicLineConfig(search);
        String name = search.contains("{") ? search.substring(0, search.indexOf("{")) : search;
        return SkillTargeter.getMythicTargeter(name, mlc);
    }
}
