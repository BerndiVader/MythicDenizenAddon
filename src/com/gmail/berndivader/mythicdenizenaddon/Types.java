package com.gmail.berndivader.mythicdenizenaddon;

public enum Types {
	caster("caster"),
	skill("skill"),
	target("target"),
	trigger("trigger"),
	power("power"),
	location("location"),
	level("level"),
	world("world"),
	repeat("repeat"),
	delay("delay"),
	activemob("activemob"),
	signal("signal"),
	entity("entity"),
	mobtype("mobtype");
	private final String val;
	Types(final String s){
		val=s;
	}
	public String a() {
		return val;
	}
}
