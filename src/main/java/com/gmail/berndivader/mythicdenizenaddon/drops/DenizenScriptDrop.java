package com.gmail.berndivader.mythicdenizenaddon.drops;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IIntangibleDrop;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.objects.Duration;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.scripts.ScriptBuilder;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.core.DetermineCommand;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.scripts.queues.core.TimedQueue;

public
class
DenizenScriptDrop
extends
Drop
implements
IIntangibleDrop
{
	final double amount;
	final String script_name;
	HashMap<String,String>attributes;
	
	public DenizenScriptDrop(String line,MythicLineConfig config,double amount) {
		super(line,config,amount);
		this.amount=amount;
		
		script_name=config.getString("script","");

		if(line.contains("{")&&line.contains("}")) {
			String parse[]=line.split("\\{")[1].split("\\}")[0].split(";");
			attributes=new HashMap<>();
			int size=parse.length;
			for(int i1=0;i1<size;i1++) {
				if(parse[i1].startsWith("script")) continue;
				String arr1[]=parse[i1].split("=");
				if(arr1.length==2) attributes.put(arr1[0],arr1[1]);
			}
		}
	}

	@Override
	public void giveDrop(AbstractPlayer abstract_player,DropMetadata drop_data) {
		List<ScriptEntry>entries=null;
		dScript script=new dScript(script_name);
		if(script!=null&&script.isValid()) {
			try {
				ScriptEntry entry=new ScriptEntry(script.getName(),new String[0],script.getContainer());
				entry.setScript(script_name);
				entries=script.getContainer().getBaseEntries(entry.entryData.clone());
			} catch (ScriptEntryCreationException e) {
				e.printStackTrace();
			}
		}
		
		if(entries!=null) {
			ScriptQueue queue;
			String id=ScriptQueue.getNextId(script.getContainer().getName());
			long req_id=DetermineCommand.getNewId();
			ScriptBuilder.addObjectToEntries(entries,"reqid",req_id);
			if(script.getContainer().contains("SPEED")) {
				long ticks=Duration.valueOf(script.getContainer().getString("SPEED","0")).getTicks();
				queue=ticks>0?((TimedQueue)TimedQueue.getQueue(id).addEntries(entries)).setSpeed(ticks):InstantQueue.getQueue(id).addEntries(entries);
			} else {
				queue=TimedQueue.getQueue(id).addEntries(entries);
			}
			HashMap<String,dObject>context=new HashMap<String,dObject>();
			context.put("player",new dPlayer((Player)abstract_player.getBukkitEntity()));
			context.put("amount",new Element(drop_data.getAmount()));
			context.put("cause",drop_data.getCause().isPresent()?new dEntity(drop_data.getCause().get().getBukkitEntity()):null);
			context.put("dropper",drop_data.getDropper().isPresent()?new dEntity(drop_data.getDropper().get().getEntity().getBukkitEntity()):null);
			queue.setContextSource(new MythicContextSource(context));
			queue.setReqId(req_id);
			for(Map.Entry<String,String>item:attributes.entrySet()) {
				queue.addDefinition(item.getKey(),item.getValue());
			}
			queue.start();
		}
	}
}
