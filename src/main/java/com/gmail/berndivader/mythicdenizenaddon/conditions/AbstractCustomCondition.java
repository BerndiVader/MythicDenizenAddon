package com.gmail.berndivader.mythicdenizenaddon.conditions;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;

public class AbstractCustomCondition extends SkillCondition {

    boolean debug;

    public AbstractCustomCondition(String line, MythicLineConfig mlc) {
        super(line);
        String action = "TRUE";
        String actionVar = "0";
        String a = mlc.getString("action", "");
        debug = mlc.getBoolean("debug", false);

        for (ConditionAction condAct : ConditionAction.values()) {
            String aa = condAct.toString();
            if (aa.toUpperCase().equals("CAST") && a.toUpperCase().startsWith("CASTINSTEAD")) {
                continue;
            }
            if (a.toUpperCase().startsWith(aa)) {
                action = aa;
                actionVar = a.substring(action.length(), a.length());
                break;
            }
        }

        try {
            this.ACTION = ConditionAction.valueOf(action.toUpperCase());
            this.actionVar = actionVar;
        } catch (Exception ex) {
            this.ACTION = ConditionAction.TRUE;
        }

        if (debug) {
            System.err.println(this.ACTION.toString() + ":" + this.actionVar);
        }
    }
}
