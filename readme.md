# Denizen Support for MythicMobs 4.x.x

#### dObjects:

##### activemob
##### mythicspawner

#### Commands for ActiveMobs:

- mmspawnmob mobtype:string location world:string level:integer save:string
  - Required: mobtype (valid mythicmob) and location as dLocation
  - Optional: world as string, level as integer and save as string
  - Returns: <entry[savename].activemob> instance of the spawned mythicmob
  
- mmcastmob [caster:dActiveMob] [target:dEntity||dLocation] [skill:string] (trigger:dEntity) (power:float)
  - Required: caster (valid activemob), target a entity or location, skill string with valid metaskill
  - Optional: trigger as Entity (default = caster), power as float (default=1)

#### Commands for Players:
  
- mmcastplayer [caster:dEntity] [skill:string] [target:dEntity||dLocation] (trigger:dEntity) (repeat:integer) (delay:integer)
  - Required: caster as dEntitiy of Player, skill string with valid metaskill, target dEntity or dLocation
  - Optional: trigger as dEntity (default = caster), repeat as integer, delay ticks as integer

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
    - mmplayercast caster:<player.entity> skill:Damage target:<player.target>
  }
  - if <player.target> == NULL {
    - mmplayercast caster:<player.entity> skill:Heal target:<player.entity> repeat:4 delay:20
  }

```
  
- mmsignal [activemob:dActiveMob] [signal:string] (trigger:dEntity)
  - Required: activemob (valid activemob), signal as string
  - Optional: trigger as entity (default = activemob)


#### Attributes for dObject world:

- world.allactivemobs
  - Returns a dList<ActiveMob> of all ActiveMobs in the given world
  
- world.allmythicspawners
  - Returns a dList<dMythicSpawner> of all MythicSpawners in the given world


#### Attributes for dObject entity:

- entity.isactivemob
  - Returns true if activemob or false if not.

- entity.activemob
  - Returns the activemob instance of the entity.

#### Attributes for dObject activemob:

- activemob.isdead
  - True or False if activemob is dead.

- activemob.hasthreattable 
  - True of False if activemob has target threattable.

- activemob.hasmythicspawner
  - Returns true if the activemob has a MythicSpawner.

- activemob.hastarget
  - Returns true if the activemob has a target

- activemob.mobtype
  - MobType of the ActiveMob as string

- activemob.displayname 
  - DisplayName of the ActiveMob as string

- activemob.location
  - location of the activemob as dLocation

- activemob.world
  - world of the activemob as dWorld

- activemob.owner
  - owner of the activemob as dEntity

- activemob.lastaggro
  - lastaggroentity of the activemob as dEntity

- activemob.toptarget
  - top target of the threattable if activemob uses them or the target of the activemob as dEntity

- activemob.uuid
  - uuid of the activemob as string

- activemob.health
  - returns the health as integer

- activemob.maxhealth
  - max possible health as integer

- activemob.faction
  - faction of the activemob as string

- activemob.stance
  - stance of the activemob as string

- activemob.level
  - level of the activemob as integer

- activemob.playerkills
  - pks of the activemob as integer

- activemob.lastsignal
  - last signal of the activemob as string
  
- activemob.mythicspawner
  - mythicspawner object if activemob has a customspawner

  
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
  
  
#### Adjustments for dObject mythicspawner:

- adjust mythicspawner activate [true||false]
  - Activate (true) or deactivate (false) the spawner
  
- adjust cooldown [integer]
  - set the cooldown in seconds
  
- adjust remainingcooldown [integer]
  - set the actual remaining cooldown in seconds
  
- adjust warmup [integer]
  - set the warmup in seconds
  
- adjust remainingwarmup [integer]
  - set the actual remaining warmup in seconds
  
- adjust mobtype [string]
  - set the mobtype of the spawner to a valid mythicmob type
  
- adjust moblevel [integer]
  - set the level of the spawner mob
  
- adjust spawn
  - Make the spawner spawn
  
- adjust attach [dActiveMob]
  - Attach the given ActiveMob to the spawner if not already attached.