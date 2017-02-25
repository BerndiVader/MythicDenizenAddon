# Denizen Support for MythicMobs 4.x.x

#### dObjects:

##### activemob
##### mythicspawner


#### Expressions for dObject world:

- world.allactivemobs
  - Returns a dList<ActiveMob> of all ActiveMobs in the given world
  
- world.allmythicspawners
  - Returns a dList<dMythicSpawner> of all MythicSpawners in the given world


#### Conditions for dObject entity:

- entity.isactivemob
  - Returns true if activemob or false if not.

- entity.activemob
  - Returns the activemob instance of the entity.

#### Conditions for dObject activemob:

- activemob.isdead
  - True or False if activemob is dead.

- activemob.hasthreattable 
  - True of False if activemob has target threattable.

- activemob.hasmythicspawner
  - Returns true if the activemob has a MythicSpawner.

- activemob.hastarget
  - Returns true if the activemob has a target

#### Expressions for dObject activemob:

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

  
#### Expressions for dObject mythicspawner:

- mythicspawner.location
  - dLocation of Spawner
  
- mythicspawner.world
  - dWorld of Spawner
  
- mythicspawner.allactivemobs
  - dList with all activemobs from spawner
  
- mythicspawner.mobtype
  - String mobtype
  
- mythicspawner.moblevel
  - Integer moblevel
  
- mythicspawner.cooldown
  - Integer cooldown
- mythicspawner.remainingcooldown
  - Integer cooldowncounter
- mythicspawner.warmup
  - Integer warmup
- mythicspawner.remainingwarmup
  - Integer warmupcounter
- mythicspawner.mobamount
  - Integer mobamount
- mythicspawner.maxmobamount
  - Integer max mob amount

  
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
