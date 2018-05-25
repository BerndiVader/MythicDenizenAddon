# Events:
[MythicMobsDeathEvent](#mythicmobsdeathevent)
[MythicMobsSpawnEvent](#mythicmobsspawnevent)
[CustomMechanic](#custommechanic)
[CustomCondition](#customcondition)

----
#### MythicMobsDeathEvent:
**Names:**
*mm denizen death*
*mythicmobs death*

|Contexts|Descriptions|
|--|--|
|`<context.victim>`|Returns the dEntity of the MythicMob being killed.|
|`<context.activemob>`|Returns the dActiveMob of the MythicMob being killed.<br>NOTE: You can use <context.victim.activemob> instead.|
|`<context.killer>`|Returns the dEntity of the killer.|
|`<context.drops>`|Returns a dList of the drops for this MythicMob.|
|`<context.money>`|Returns an Element(Decimal) of the money dropped.|
|`<context.exp>`|Returns an Element(Number) of the XP dropped by the mob.|

**Determinations:**
`DROPS:dList(dItem)` to specify new items to be dropped.
`MONEY:Element(Decimal)` to specify the new amount of money to drop. Requires Vault and an economy plugin.
`EXP:Element(Number)` to specify the new amount of XP to drop.

**Determination Examples:**
```yaml
  on mythicmobs death:
    - determine passively drops:li@
    - determine money:0;exp:0
    
  on mm denizen death:
  on mythicmobs death:
    - determine drops:@li;money:0;exp:0
    
  on mm denizen death:
  on mythicmobs death:
    - determine passively money:0
    - determine exp:0
    - determine drops:@li
```
----
#### MythicMobsSpawnEvent:
**Names**
*mm denizen spawn*
*mythicmobs spawn*

|Contexts|Descriptions|
|--|--|
|<context.entity>|Returns the dEntity of the MythicMob being spawned.|
|<context.location>|Returns the dLocation where the MythicMob is being spawned.|
|<context.level>|Returns the level of the MythicMob being spawned.|
|<context.mobtype>|Returns the mob type of the MythicMob being spawned.<br>NOTE: You can use <context.entity.entity_type> instead.|
|<context.cancelled><br><context.iscancelled>|Returns an Element(Boolean) if the event is cancelled.|

**Determinations:**
"`TRUE`" to cancel the event. (This allows use of the tag `<context.cancelled>`).

----
#### CustomMechanic:
**Names:**
*on mm denizen mechanic*

**Note:**
The event fires only when a MythicMob uses the `dskill` skill.

|Contexts|Descriptions|
|---|---|
|`<context.skill>`|Returns an Element(String) of the skill name specified by the `dskill` MythicMobs skill.|
|`<context.args>`|Returns the parameters for the skill as specified by the `dskill` MythicMobs skill.|
|`<context.caster>`|Returns a dEntity of the entity who cast this skill.|
|`<context.target>`|Returns the dEntity of the target entity, if any.|
|`<context.targetlocation>`|Returns the dLocation of the target location, if any.|
|`<context.targettype>`|Returns "NONE" if there is no target, "ENTITY" if the target is an entity, or "LOCATION" if the target is a location.|
|`<context.trigger>`|Returns the dEntity of the entity who triggered the skill.|

**MythicMob and Denizen Examples:**
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
#### CustomCondition:
**Names:**
*on mm denizen condition*
*mm denizen targetcondition*

**Note:**
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
    if <context.type> == "l" {
        - if <context.args> != <context.location.world.time.period> {
        - determine false
      }
    }
    }
    - if <context.condition> == "weather" {
      - if <context.args> == "sunny" {
      - if <context.type> == "e" {
        - if <context.entity.world.has_storm> == true {
          - determine false
        }
      }
      - if <context.type> == "l" {
        - if <context.location.world.has_storm> == true {
          - determine false
        }
      }
    }
    }
```