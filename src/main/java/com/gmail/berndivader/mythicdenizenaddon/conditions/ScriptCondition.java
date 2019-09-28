package com.gmail.berndivader.mythicdenizenaddon.conditions;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptCondition<E extends ObjectTag> extends AbstractCustomCondition {

    static String determinationStr = "_determination";
    final String scriptName;
    HashMap<String, String> attributes;

    public ScriptCondition(String line, MythicLineConfig mlc) {
        super(line, mlc);

        scriptName = mlc.getString("script", "");

        if (line.contains("{") && line.contains("}")) {
            String[] parse = line.split("\\{")[1].split("}")[0].split(";");
            attributes = new HashMap<>();

            for (String s : parse) {
                if (s.startsWith("script")) {
                    continue;
                }
                String arr1[] = s.split("=");
                if (arr1.length == 2) attributes.put(arr1[0], arr1[1]);
            }
        }
    }

    boolean check(E denizenSource) {
        return check(denizenSource, null);
    }

    boolean check(E denizenSource, E denizenTarget) {
        boolean match = false;
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

        if (entries == null) {
            return false;
        }

        String id = ScriptQueue.getNextId(script.getContainer().getName());
        long req_id = 0L;
        ScriptBuilder.addObjectToEntries(entries, "reqid", req_id);

        HashMap<String, ObjectTag> context = new HashMap<>();
        context.put("source", denizenSource);
        if (denizenTarget != null) context.put("target", denizenTarget);

        ScriptQueue queue = InstantQueue.getQueue(id).addEntries(entries);
        queue.setContextSource(new MythicContextSource(context));
        for (Map.Entry<String, String> item : attributes.entrySet()) {
            queue.addDefinition(item.getKey(), item.getValue());
        }

        final ScriptEntry final_entry = entry;
        queue.callBack(() -> final_entry.setFinished(true));
        queue.start();

        List<String> args = queue.getLastEntryExecuted().getArguments();
        for (String s1 : args) {
            Argument arg = new Argument(s1);
            if (arg.matchesPrimitive(PrimitiveType.Boolean)) {
                match = arg.asElement().asBoolean();
                break;
            }
        }
        return match;
    }
}
