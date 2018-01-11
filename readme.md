# Denizen Support for MythicMobs 4.0.1 or higher

- Update 0.499  - even more rework
- Update 0.498  - more rework
- Update 0.497c - added mmskillcast command & some more fixes
- Update 0.497b - some fixes & changes
- Update 0.497a - added "mm denizen targetcondition" event
- Update 0.496a - added 1.12.1 support
- Update 0.495a - added mmapplymythic & mmremovemythic command
- Update 0.493a - 1.12 support and build with denizen 1.0.2
- Update 0.492a - Read updatev0492a.txt for more details!


#### dObjects:

##### activemob
##### mythicspawner
##### mythicmob
##### team


## Events:


#### MythicMobsDeathEvent:

##### mm denizen death
##### mythicmobs death

- **returns:** context.activemob context.victim context.killer context.drops context.money context.exp


- *victim* = dEntity of victim
- *activemob* = dActiveMob of victim
- *killer* = dEntity of killer
- *drops* = mythicmobs drops of event as list
- *money* = money drop as value
- *exp* = exp drop as value


- **determine:** change drops money exp values of the event: Use a list separeted by ";" Valid entries are: drops:dList;money:Value;exp:Value All entries of the list are optional. Additinal info: determine should always be called at the end of the code because of the fact that it will stop executing the rest.

```
Examples:

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
  

#### MythicMobsSpawnEvent:

##### mm denizen spawn
##### mythicmobs spawn

- **returns:** context.entity context.location context.level context.mobtype context.cancelled


- *entity* = dEntity of activemob
- *location* = dLocation
- *level* = Integer value of the moblevel
- *mobtype* = Mobtype of activemob as string
- *cancelled or iscancelled* = Boolean value if event is cancelled or not. (cant be undo)


- **determine:** event can be cancelled by determine true

  
#### CustomMechanics:

##### on mm denizen mechanic:

- **returns:** context.skill context.args context.caster context.target context.targetlocation context.targettype context.trigger

 
- *skill* = denizen skillname
- *args* = skill parameter
- *caster* = dEntity of the caster
- *target* = dEntity of target if target = entity
- *targetlocation* = dLocation of target if target = location
- *targettype* = returns NONE for no targetskill, ENTITY for entitytarget or LOCATION for locationtarget
- *trigger* = dEntity of trigger


- the event is fired if mythicmobs run the skill named "dskill"
  
```
Example:

mythicmobs mob yml:

denizenmob:
  Type: cow
  Display: 'a MythicMobs Monkey'
  Skills:
  - dskill{s=skillname;a=this,is,for,the,args} @trigger ~onInteract 1
  
denizen script:

	on mm denizen mechanic:
	  - narrate <context.skill>
	  - narrate <context.args>
	  - narrate <context.caster>
	  - narrate <context.target>
	  - narrate <context.targetlocation>
	  - narrate <context.trigger>
	  - narrate <context.targettype>

```

#### CustomConditions:

##### on mm denizen condition / mm denizen targetcondition:

- **Returns:** context.type context.entity context.location context.condition context.args context.meet


- *type* is "e" for entity condition or "l" for location condition
- *entity* if type = e : the entity can be the caster or if used as targetcondtion the target
- *targetentity* (if targetcondition) = the entity of the mythicmobs targeter used in the skill.
- *location* if type = l : the location to work with
- *targetlocation* (if targetcondition) = location of the mythicmobs targeter used in the skill.
- *condition* the name of the condition.
- *args* the arguments for the condition.


- **determine:** true or false where true meet the condition


- event is fired if in the skill yml under conditions the dcondition is present.
  
```
Example:

for RandomSpawners (works with MyhticMobs SnapShot 2105 or higher): 

RandomExampleSpawner:
  Mobname: SkeletonKing
  Worlds: world
  Chance: 1
  Priority: 1
  Action: replace
  Conditions:
  - dspawncondition{c=weather;args=sunny}
  - dspawncondition{c=time;args=day}
  
For Skills:

niceweather:
  Conditions:
  - dcondition{c=weather;args=sunny}
  - dcondition{c=time;args=day}
  Skills:
  - message{msg="Nice weather today, isnt it <target.name>?"}
  
For Denizen:

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

#### Commands for ActiveMobs:

##### mmapplymythic entity:dEntity mobtype:string level:integer
+ **Required:** entity - any living entity and mobtype - the name of the mythicmobs mob
+ *Optional:* level - apply a level to the new mythicmob
+ *Returns:* <entry[savename].activemob> intance of the transformed mythicmobs mob
  
##### mmremovemythic activemob:dActiveMob
- **Required:** activemob - any mythicmobs mob that should be transformed into an ordinary entity
- *Returns:* <entry[savename].entity> intance of the transformed mythicmobs mob

##### mmspawnmob mobtype:string location world:string level:integer save:string
- **Required:** mobtype (valid mythicmob) and location as dLocation
- *Optional:* world as string, level as integer and save as string
- *Returns:* <entry[savename].activemob> instance of the spawned mythicmob
  
##### mmcastmob caster:dActiveMob target:dEntity||dLocation skill:string trigger:dEntity power:float
- **Required:** caster (valid activemob), target a entity or location, skill string with valid metaskill
- *Optional:* trigger as Entity (default = caster), power as float (default=1)
  
##### mmskillcast caster:dEntity target:dEntity||dLocation skill:string trigger:dEntity power:float repeat:int delay:int
- **Required:** caster (valid activemob), target a entity or location, skill string with valid metaskill
- *Optional:* trigger as Entity (default = caster), power as float (default=1)
  
##### mmsignal activemob:dActiveMob singal:string trigger:dEntity
- **Required:** activemob (valid activemob), signal as a string
- *Optional:* trigger as dEntity (default = self)

##### mmtrigger activemob:dActiveMob trigger:string entity:dEntity
- **Required:** activemob <dActiveMob>, trigger <string>, entity <dEntity> the entity which trigger the activemob
- trigger the <activemob> with trigger <string> and triggerenttiy <dEntity>
- Valid triggers are: DEFAULT, ATTACK, BOW_HIT, BLOCK, COMBAT, CROUCH, UNCROUCH, DAMAGED, DROPCOMBAT, DEATH, ENTERCOMBAT, EXPLODE, INTERACT, KILL, KILLPLAYER, PLAYERDEATH, SHOOT, SIGNAL, SPAWN, SPLASH_POTION, SWING, TARGETCHANGE, TELEPORT, TIMER, USE, READY
  
```
	on entity damaged by projectile:
	  - if <context.entity.isactivemob> == true && <context.projectile.name> == "arrow" {
	    - mmtrigger activemob:<context.entity.activemob> trigger:boghit entity:<context.damager>
	  }
	  
MythicMobs part:

Monkey:
  Type: zombie
  Display: "MythicMobs Monkey"
  Health: 40
  Armor: 10
  Skills:
  - message{msg="Ouch! Stop shooting arrows at me!"} @trigger ~onBowHit 1
```

mmapplymythic & mmremovemythic example:

```
    on player right clicks entity:
		- if <context.entity.isactivemob> == false {
			- mmapplymythic entity:<context.entity> mobtype:MythicEntity level:1 save:result
			- mmtrigger activemob:<entry[result].activemob> trigger:SPAWN entity:<context.entity>
			- announce "Applied mythic to entity"
		} else {
			- mmremovemythic activemob:<context.entity.activemob>
			- announce "Removed mythic from entity"
		}
		
mob yaml:

MythicEntity:
  Type: zombie
  Skills:
  - message{msg="Me a MythicMobs mob now!"} @world ~onSpawn
```


#### Commands for Players:
  
##### mmcastplayer caster:dEntity skill:string target:dEntity||dLocation trigger:dEntity repeat:integer delay:integer
- **Required:** caster as dEntitiy of Player, skill string with valid metaskill, target dEntity or dLocation
- *Optional:* trigger as dEntity (default = caster), repeat as integer, delay ticks as integer

```
mythicmobs skill yml:

Heal:
  Skills:
  - effect:particles{particle=heart;amount=8;vSpread=0.5;hSpread=0.5;Spped=0.01;yoffset=1} @self
  - heal{a=2;overheal=false} @self

Damage:
  TargetConditions:
  - lineofsight true
  - distance{d=<10} true
  - distance{d=>3} true
  Skills:
  - effect:particleline{particle=lava;amount=20;vSpread=0.10;hSpread=0.10;Speed=0.2;yoffset=1;ystartoffset=0;distancebetween=1} @target
  - damage @target
  
Denizen script:

on player clicks:
  - if <player.target> != NULL {
    - mmcastplayer caster:<player.entity> skill:Damage target:<player.target>
  }
  - if <player.target> == NULL {
    - mmcastplayer caster:<player.entity> skill:Heal target:<player.entity> repeat:4 delay:20
  }

```
  
#### Attributes for dObject world:

##### world.allactivemobs
- Returns a dList of all ActiveMobs in the given world
  
##### world.allmythicspawners
- Returns a dList of all MythicSpawners in the given world


#### Attributes for dObject entity:

##### entity.isactivemob
- Returns true if activemob or false if not.

##### entity.activemob
- Returns the activemob instance of the entity.
  
##### entity.damage
- returns Element(double)
- Base damage (>1.9 only)

##### entity.followrange
+ returns Element(double)
+ Base followrange (>1.9 only)

##### entity.armor
- returns Element(double)
- Base armor (>1.9 only)
	
##### entity.attackspeed
- returns Element(double)
- Base attackspeed (>1.9 only) 

##### entity.knockbackresist
- returns Element(double)
- Base knockback resistance (>1.9 only)
	
##### entity.jumpstrength
- returns Element(double)
- Base jump strength if entity of horse type (>1.9 only)
	
##### entity.maxnodamageticks
- returns Element(integer)
- Max amount of nodamageticks
	
##### entity.nodamageticks
- returns Element(integer)
- Actual nodamageticks
	
##### entity.mmtargets[<String>]
- Returns dList<dEntity||dLocation>
- Use any MythicMobs targeter in <String>
- Example:
    
```yaml
	on player right clicks:
	  - announce <player.entity.mmtargets[@EIR{r=30}]>
	  ## returns a dEntity dList
	  - announce <player.entity.mmtargets[@Ring{r=10;p=5}]>
	  ## returns a dLocation dList
```
	
#### Attributes for dObject activemob:

##### activemob.isdead
- True or False if activemob is dead.

##### activemob.hasthreattable 
- True of False if activemob has target threattable.

##### activemob.hasmythicspawner
- Returns true if the activemob has a MythicSpawner.

##### activemob.hastarget
- Returns true if the activemob has a target

##### activemob.mobtype
- MobType of the ActiveMob as string

##### activemob.displayname 
- DisplayName of the ActiveMob as string

##### activemob.location
- location of the activemob as dLocation

##### activemob.world
- world of the activemob as dWorld

##### activemob.owner
- owner of the activemob as dEntity

##### activemob.lastaggro
- lastaggroentity of the activemob as dEntity

##### activemob.toptarget
- top target of the threattable if activemob uses them or the target of the activemob as dEntity

##### activemob.uuid
- uuid of the activemob as string

##### activemob.health
- returns the health as integer

##### activemob.maxhealth
- max possible health as integer

##### activemob.faction
- faction of the activemob as string

##### activemob.stance
- stance of the activemob as string

##### activemob.level
- level of the activemob as integer

##### activemob.playerkills
- pks of the activemob as integer

##### activemob.lastsignal
- last signal of the activemob as string
  
##### activemob.mythicspawner
- mythicspawner object if activemob has a customspawner
  
##### activemob.threattable
- returns dList of dEntity in activemobs threattable
  
##### activemob.threatvalueof[<dEntity>]
- returns threat amount as double of dEntity out of activemobs threattable
  
##### activemob.damage
- returns Element(double)
- max possible amount of damage the mob can make.
	
##### activemob.power
- returns Element(float)
- Powerlevel of the mob
	
##### activemob.lastdamageskillamount
- returns Element(double)
- Amount of damage from the last used damage skill.

##### activemob.hasimmunitytable
- returns Element(boolean)
- Check if the mob has immunitytable enabled.

##### activemob.isonimmunitycooldown[dEntity]
- returns Element(boolean)
- Check if the entity is on the mob's immunitytable.

  
#### Attributes for dObject mythicspawner:

- mythicspawner.location
  - Location of Spawner
  
- mythicspawner.world
  - World of Spawner
  
- mythicspawner.allactivemobs
  - List with all activemobs from spawner
  
- mythicspawner.mobtype
  - String MythicMob type
  
- mythicspawner.moblevel
  - Integer level of the mob
  
- mythicspawner.cooldown
  - Integer cooldown in seconds
  
- mythicspawner.remainingcooldown
  - Integer remaining cooldown in seconds
  
- mythicspawner.warmup
  - Integer warmup in seconds
  
- mythicspawner.remainingwarmup
  - Integer remeining warmup seconds
  
- mythicspawner.mobamount
  - Integer amount of spawned and living activemobs
  
- mythicspawner.maxmobamount
  - Integer Max amount of mobs the spawner can spawn

  
#### Adjustments for dObject activemob:

- adjust activemob remove 
  - Removes & unregister the ActiveMob from MythicMobs.

- adjust activemob gcd:integer
  - set GCD

- adjust activemob displayname:string
  - set displayname

- adjust activemob owner:string
  - set owner (not does not set the bukkitowner)

- adjust activemob target:dEntity
  - set target

- adjust activemob faction:string
  - set faction

- adjust activemob stance:string
  - set stance

- adjust activemob level:integer
  - set level

- adjust activemob playerkills:integer
  - set playerkills

- adjust activemob health:integer
  - set health
  
- adjust activemob maxhealth:integer
  - set max health
  
- adjust activemob incthreat:<dEntity>|amount
  - add amount to threat dEntity of activemob
  
- adjust activemob decthreat:<dEntity>|amount
  - dec amount to threat dEntity of activemob
  
- adjust activemob removethreat:<dEntity>
  - remove dEntity from activemobs threattable
  
- adjust activemob clearthreat
  - clear the whole threattable
  
- adjust activemob newtargetthreattable
  - search a new target for activemobs with threattable
  
- adjust activemob getnewtarget	
  - search new target for activemobs without threattable

- adjust dActiveMob setimmunitycooldown:dEntity
    - set the mobs immunitycooldown for the entity.
  

#### Adjustments for dObject dEntity:

- adjust dEntity followrange:double
    - set follow range
	
- adjust dEntity damage:double
    - set base damage

- adjust dEntity armor:double
    - set base armor (>1.9 only)
	
- adjust dEntity attackspeed:double
    - set attack speed (>1.9 only)

- adjust dEntity knockbackresist:double
    - set knockback resistance
  
  
#### Adjustments for dObject mythicspawner:

- adjust mythicspawner activate true||false
  - Activate (true) or deactivate (false) the spawner
  
- adjust cooldown integer
  - set the cooldown in seconds
  
- adjust remainingcooldown integer
  - set the actual remaining cooldown in seconds
  
- adjust warmup integer
  - set the warmup in seconds
  
- adjust remainingwarmup integer
  - set the actual remaining warmup in seconds
  
- adjust mobtype string
  - set the mobtype of the spawner to a valid mythicmob type
  
- adjust moblevel integer
  - set the level of the spawner mob
  
- adjust spawn
  - Make the spawner spawn
  
- adjust attach dActiveMob
  - Attach the given ActiveMob to the spawner if not already attached.
  
  
## Scoreboard Teams Support:

#### Commands:

- createTeam name:string
  - createteam name:TeamName save:store]
  - Returns <entry[store].team>
  - Use this command to create a new team  on the main scoreboard. Max length of the teamname is 16 chars.
  
- getAllTeams
  - getallteams save:teamlist
  - This example returns a dlist of all teams from the main scoreboard.
  
- getEntityByEntry entry:string 
  - getentitybyentry entry:uuid||playername save:store
  - Returns <entry[store].entity> Get the entity of the team entry.
  - Returns false if the entry is no longer present: Is removed or offline in case of a player.
  
- getTeam
  - name:string
  - getteam name:TeamName save:store
  - Returns <entry[store].team> dTeam instance of the given team if present.
  
- removeTeam
  - name:string
  - removeteam name:TeamName
  - Remove the team from the main scoreboard.
  
#### dEntity Attributes:

- dEntity.hasteam
  - boolean
  - Returns true if the entity is in a team or false if not.
  
- dEntity.team
  - dTeam
  - Get the dTeam instance of entities team.

#### dEntity Adjustments:

- jointeam
  - string
  - adjust dEntity jointeam:teamname
  - Make the entity join the given team. The entity will be removed from any other team and join the new team.
  
- leaveteam
  - adjust dEntity leaveteam
  - Remove the entity from the team it is part of.
  
#### dTeam Attributes:

- dTeam.name
  - string
  - Returns the internal name of the team.
  
- dTeam.displayname
  - string
  - Returns the display name of the team.
  
- dTeam.members
  - dList string
  - Returns a string dlist with the entries of the team. UUID's for the entities and if instanceof player the player name.
  
- dTeam.collision
  - string
  - Returns the OptionType of the COLLISION_RULE.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- dTeam.deathmessage
  - string
  - Returns the OptionType of DEATH_MESSAGE_VISIBILITY.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- dTeam.nametag
  - string
  - Returns the OptionType of NAME_TAG_VISIBILITY.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- dTeam.friendlyfire
  - boolean
  - Retruns true or false whatever friendlyfire is on or off.
  
- dTeam.friendlyinvisibles
  - boolean
  - Returns true or false if invisible teammembers shall be visible to all members.
  
- dTeam.prefix
  - string
  - Get the prefix of the team.
  
- dTeam.suffix
  - string
  - Get the suffix of the team.
  
#### dTeam Adjustments:

- addmember
  - dEntity
  - adjust dTeam addmember:dEntity
  - Add the dEntity to the dTeam.
  
- delmember
  - string
  - adjust dTeam delmember:EntryName
  - Remove the entry from the dTeam. If only the dEntity is present, use getentitybyentry command to get the entryname of the entity.
  
- displayname
  - string
  - adjust dTeam displayname:newName
  - Change the displayname of the team.
  
- collision
  - string
  - adjust dTeam collision:OptionType
  - Change the COLLISION_RULE for the team.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- deathmessage
  - string
  - adjust dTeam deathmessage:OptionType
  - Change the DEATH_MESSAGE_VISIBILITY.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- nametag
  - string
  - adjust dTeam nametag:OptionType
  - Change the NAME_TAG_VISIBILITY.
  - Possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
  
- friendlyinvisibles
  - boolean
  - adjust dteam friendlyinvisibles:boolean
  - Change if invisible teammembers are shown to all teammembers.
  - Possible values: true/false
  
- friendlyfire
  - boolean
  - adjust dTeam friendlyfire:boolean
  - Enable or disable friendlyfire.
  - Possible values: true/false
  
- prefix
  - string
  - adjust dTeam prefix:newprefix
  - Set the prefix for the team. Default is empty.
  
- suffix
  - string
  - adjust dTeam suffix:newsuffix
  - Set the suffix for the team. Default is empty.
  