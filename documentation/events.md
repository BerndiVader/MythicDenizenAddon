# Events:
[MythicMobsDeathEvent](#mythicmobsdeathevent) <br>
[MythicMobsSpawnEvent](#mythicmobsspawnevent) <br>
[MythicMobsDropEvent](mythicmobsdropevent) <br>
[CustomMechanic](#custommechanic) <br>
[CustomCondition](#customcondition) <br>
[EntityTargeter](#entitytargeter) <br>
[LocationTargeter](#locationtargeter) <br>

----
### MythicMobsDeathEvent
**Names:** <br>
*mm denizen death* <br>
*mythicmobs death*

|Contexts|Descriptions|
|--|--|
|`<context.victim>`<br>`<context.entity>`|Returns the dEntity of the MythicMob being killed.|
|`<context.activemob>`|Returns the dActiveMob of the MythicMob being killed.<br>NOTE: You can use `<context.victim.activemob>` instead.|
|`<context.killer>`<br>`<context.attacker>`|Returns the dEntity of the killer.|
|`<context.drops>`|Returns a dList of the drops for this MythicMob.|
|`<context.money>`|Returns an Element(Decimal) of the money dropped.|
|`<context.exp>`|Returns an Element(Number) of the XP dropped by the mob.|

**Determinations:** <br>
*DETERMINATIONS DONT WORK ANYMORE BECAUSE MYTHICMOBS DEATHEVENT IS BROKEN*
`DROPS:dList(dItem)` to specify new items to be dropped. <br>
`MONEY:Element(Decimal)` to specify the new amount of money to drop. Requires Vault and an economy plugin. <br>
`EXP:Element(Number)` to specify the new amount of XP to drop. <br>

**Determination Examples:**
```yaml
  on mythicmobs death:
    - determine passively drops:li@
    - determine money:0;exp:0
    
  on mm denizen death:
  on mythicmobs death:
    - determine drops:li@;money:0;exp:0
    
  on mm denizen death:
  on mythicmobs death:
    - determine passively money:0
    - determine passively exp:0
    - determine drops:li@
```
----
### MythicMobsSpawnEvent
**Names** <br>
*mm denizen spawn* <br>
*mythicmobs spawn* <br>

|Contexts|Descriptions|
|--|--|
|`<context.entity>`|Returns the dEntity of the MythicMob being spawned.|
|`<context.location>`|Returns the dLocation where the MythicMob is being spawned.|
|`<context.level>`|Returns the level of the MythicMob being spawned.|
|`<context.mobtype>`|Returns the mob type of the MythicMob being spawned.<br>NOTE: You can use `<context.entity.entity_type>` instead.|
|`<context.cancelled>`<br>`<context.iscancelled>`|Returns an Element(Boolean) if the event is cancelled.|

**Determinations:** <br>
"`TRUE`" to cancel the event. (This allows use of the tag `<context.cancelled>`).

----
### MythicMobsDropEvent
**Names** <br>
*mm lootdrop* <br>
*mythicmobs lootdrop* <br>

|Contexts|Descriptions|
|--|--|
|`<context.drops>`|Returns the dList of all drops inside the Lootbag.|
|`<context.money>`|Returns the amount of money found in the Lootbag.|
|`<context.exp>`|Returns the amount of exp found in the Lootbag.|
|`<context.activemob>`|Returns the MythicMobs mob instance.|
|`<context.killer>`|Returns the killer of the mob.|

**Determinations:** <br>
`dList` Replace or clear the drops.

**Denizen Example:** <br>
```yaml
MM_Events:
	type: world
	debug: false
  
	events:
		on mythicmobs lootdrop:
			- announce <context.drops>
			- announce <context.activemob.mobtype>
			- announce <context.killer.name>
			- define money "money 5 1"
			- define exp "exp 100 1"
			- define droptable "ClothesTD 1 1"
			- determine li@i@dirt|i@stone|<def[money]>|<def[exp]>|<def[droptable]>
```

----
### EntityTargeter
**Names** <br>
*mythicmobs entitytargeter* <br>

**Note:** <br>
The event fires only when a MythicMob uses the `dentity` targeter.

|Contexts|Descriptions|
|--|--|
|`<context.targeter>`|Returns the name of the targeter defined inside the dentity targeter.|
|`<context.caster>`|Get the caster as entity.|
|`<context.trigger>`|The trigger as entity.|
|`<context.origin>`|The origin location.|
|`<context.cause>`|What caused the skill.|
|`<context.args>`|The args as list defined inside the dentity targeter.|

**Determinations:** <br>
`dList` Add entities as targets to use within mythicmobs.

**MythicMob and Denizen Examples:** <br>
*In MythicMobs:*
```yaml
PotatoMob:
  Type: zombie
  Display: 'a Potato'
  Skills:
  - message{msg="FRENCH FRIES!"} @dentity{name=denizen_entity_targeter;args=arg1,arg2,arg3,arg4} ~onInteract
```
*In Denizen:*
```yaml
		on mythicmobs entitytargeter:
			- narrate <context.targeter>
			- narrate <context.caster>
			- narrate <context.trigger>
			- narrate <context.origin>
			- narrate <context.cause>
			- narrate <context.args>
			- determine <context.caster.location.find.players.within[5.0]>
```

----
### LocationTargeter
**Names** <br>
*mythicmobs locationtargeter* <br>

**Note:** <br>
The event fires only when a MythicMob uses the `dlocation` targeter.

|Contexts|Descriptions|
|--|--|
|`<context.targeter>`|Returns the name of the targeter defined inside the dentity targeter.|
|`<context.caster>`|Get the caster as entity.|
|`<context.trigger>`|The trigger as entity.|
|`<context.origin>`|The origin location.|
|`<context.cause>`|What caused the skill.|
|`<context.args>`|The args as list defined inside the dentity targeter.|

**Determinations:** <br>
`dList` Add locations as targets to use within mythicmobs.

**MythicMob and Denizen Examples:** <br>
*In MythicMobs:*
```yaml
PotatoMob:
  Type: zombie
  Display: 'a Potato'
  Skills:
  - message{msg="FRENCH FRIES!"} @dlocation{name=denizen_location_targeter;args=arg1,arg2,arg3,arg4} ~onInteract
```
*In Denizen:*
```yaml
		on mythicmobs locationtargeter:
			- narrate <context.targeter>
			- narrate <context.caster>
			- narrate <context.trigger>
			- narrate <context.origin>
			- narrate <context.cause>
			- narrate <context.args>
```

----
### CustomMechanic
**Names:** <br>
*on mm denizen mechanic* <br>
*on mythicmobs skill* <br>

**Note:** <br>
The event fires only when a MythicMob uses the `dskill` skill.

|Contexts|Descriptions|
|---|---|
|`<context.skill>`|Returns an Element(String) of the skill name specified by the `dskill` MythicMobs skill.|
|`<context.args>`|Returns the parameters for the skill as specified by the `dskill` MythicMobs skill.|
|`<context.caster>`<br>`<context.entity>`|Returns a dEntity of the entity who cast this skill.|
|`<context.target>`|Returns the dEntity of the target entity, if any.|
|`<context.targetlocation>`|Returns the dLocation of the target location, if any.|
|`<context.targettype>`|Returns "NONE" if there is no target, "ENTITY" if the target is an entity, or "LOCATION" if the target is a location.|
|`<context.trigger>`|Returns the dEntity of the entity who triggered the skill.|

**MythicMob and Denizen Examples:** <br>
*In MythicMobs:*
```yaml
denizenmob:
  Type: cow
  Display: 'a MythicMobs Monkey'
  Skills:
  - dskill{s=skillname;a=this,is,for,the,args} @trigger ~onInteract 1
```
*In Denizen:*
```yaml
  on mm denizen mechanic:
    - narrate <context.skill>
    - narrate <context.args>
    - narrate <context.caster>
    - narrate <context.target>
    - narrate <context.targetlocation>
    - narrate <context.trigger>
    - narrate <context.targettype>
```

----
### CustomCondition
**Names:** <br>
*on mm denizen condition* <br>
*mm denizen targetcondition* <br>

**Note:** <br>
This event is fired a MythicMobs skill uses a dcondition.

|Contexts|Descriptions|
|---|---|
|`<context.type>`|Returns "e" for entity conditions, and "l" for location conditions.|
|`<context.entity>`|Returns a dEntity, if the condition is am entity.|
|`<context.targetentity>`|Returns the dEntity of the MythicMobs targeter used in the skill, if targetcondition.|
|`<context.location>`|Returns a dLocation, if the condition is a location.|
|`<context.args>`|Returns the arguments for the condition.|

**Determinations:**
`Element(Boolean)` to determine if the condition is met.

**MythicMob and Denizen Examples:**
*In MythicMobs:*
```yaml
# Works with MythicMobs snapshot 2105 or higher

# An example spawner
RandomExampleSpawner:
  Mobname: SkeletonKing
  Worlds: world
  Chance: 1
  Priority: 1
  Action: replace
  Conditions:
  - dspawncondition{c=weather;args=sunny}
  - dspawncondition{c=time;args=day}
  
# An example skill (in skills.yml or similar)
niceweather:
  Conditions:
  - dcondition{c=weather;args=sunny}
  - dcondition{c=time;args=day}
  Skills:
  - message{msg="Nice weather today, isnt it <target.name>?"}
```
*In Denizen:*
```yaml
    on mm denizen condition:
    - if <context.condition> == "time" {
      - if <context.type> == "e" {
        - if <context.args> != <context.entity.world.time.period> {
          - determine false
        }
      }
      - if <context.type> == "l" {
        - if <context.args> != <context.location.world.time.period> {
          - determine false
        }
      }
    }
    else if <context.condition> == "weather" {
      - if <context.args> == "sunny" {
        - if <context.type> == "e" {
          - if <context.entity.world.has_storm> == true {
            - determine false
          }
        }
      }
      else if <context.type> == "l" {
        - if <context.location.world.has_storm> == true {
          - determine false
        }
      }
    }
```
