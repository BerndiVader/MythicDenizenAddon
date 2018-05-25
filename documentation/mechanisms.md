# Mechanisms
[ActiveMob mechanisms](#dactivemob-mechanisms) <br>
[Entity mechanisms](#dentity-mechanisms) <br>
[MythicSpawner mechanisms](#dmythicspawner-mechanisms) <br>
[Team mechanisms](#dteam-mechanisms)

### dActiveMob Mechanisms
|Mechanisms|Descriptions|
|---|---|
|Name: `remove`<br>Input: `None`|Removes and unregisters the ActiveMob from MythicMobs.<br>**Related tags:** `none`|
|Name: `gdc`<br>Input: `Element(Number)`|Sets the GCD of this ActiveMob.<br>**Related tags:** `none`|
|Name: `displayname`<br>Input: `Element(String)`|Sets the display name of this ActiveMob.<br>**Related tags:** `<activemob@activemob.displayname>`|
|Name: `owner`<br>Input: `dEntity`|Sets the owner of this ActiveMob. Does not set the owner via Bukkit.<br>**Related tags:** `<activemob@activemob.owner>`|
|Name: `target`<br>Input: `dEntity`|Sets the target of this ActiveMob.<br>**Related tags:** `<activemob@activemob.hastarget>`, `<activemob@activemob.toptarget>`|
|Name: `faction`<br>Input: `Element(String)`|Sets the faction of this ActiveMob.<br>**Related tags:** `<activemob@activemob.faction>`|
|Name: `stance`<br>Input: `Elemenet(String)`|Sets the stance of this ActiveMob.<br>**Related tags:** `<activemob@activemob.stance>`|
|Name: `level`<br>Input: `Element(Number)`|Sets the level of this ActiveMob.<br>**Related tags:** `<activemob@activemob.level>`|
|Name: `playerkills`<br>Input: `Element(Number)`|Set the amount of players this ActiveMob has killed. Does not actually randomly slay players to achieve this number.<br>**Related tags:** `<activemob@activemob.playerkills>`|
|Name: `health`<br>Input: `Element(Number)`|Sets the health of this ActiveMob.<br>**Related tags:** `<activemob@activemob.health>`|
|Name: `maxhealth`<br>Input: `Element(Number)`|Sets the maximum health of this ActiveMob.<br>**Related tags:** `<activemob@activemob.maxhealth>`|
|Name: `incthreat`<br>Input: `dEntity|Element(Decimal)`|Adds an amount of threat to the specified entity.<br>**Related tags:** `<activemob@activemob.hasthreattable>`, `<activemob@activemob.threattable>`, `<activemob@activemob.threatvaleuof[<entity>]>`|
|Name: `decthreat`<br>Input: `dEntity|Element(Decimal)`|Removes an amount of threat from the specified entity.<br>**Related tags:** `<activemob@activemob.hasthreattable>`, `<activemob@activemob.threattable>`, `<activemob@activemob.threatvaleuof[<entity>]>`|
|Name: `removethreat`<br>Input: `dEntity`|Removes the specified entity from this ActiveMob's ThreatTable.<br>**Related tags:** `<activemob@activemob.hasthreattable>`, `<activemob@activemob.threattable>`|
|Name: `clearthreat`<br>Input: `none`|Clear the entire ThreatTable for this ActiveMob.<br>**Related tags:** `<activemob@activemob.hasthreattable>`, `<activemob@activemob.threattable>`|
|Name: `newtargetthreattable`<br>Input: `none`|Gets a new target for this ActiveMob using the ThreatTable, if any.<br>**Related tags:** `<activemob@activemob.hasthreattable>`, `<activemob@activemob.threattable>`, `<activemob@activemob.threatvaleuof[<entity>]>`|
|Name: `getnewtarget`<br>Input: `none`|Gets a new target for this ActiveMob without using a ThreatTable.<br>**Related tags:** `<activemob@activemob.hastarget>`, `<activemob@activemob.toptarget>`|
|Name: `setimmunitycooldown`<br>Input: `dEntity`|Sets the immunity cooldown for the specified entity.<br>**Related tags:** `<activemob@activemob.hasimmunitytable>`, `<activemob@activemob.isonimmunitycooldown[<entity>]>`|

----
### dEntity Mechanisms
|Mechanisms|Descriptions|
|---|---|
|Name: `followrange`<br>Input: `Element(Decimal)`|Sets the base following range of this entity.<br>**Related tags:** `<e@entity.followrange>`|
|Name: `damage`<br>Input: `Element(Decimal)`|Sets the base damage of this entity.<br>**Related tags:** `<e@entity.damage>`|
|Name: `armor`<br>Input: `Element(Decimal)`|Sets the base armor of this entity.<br>**Related tags:** `<e@entity.armor>`<br>**Note:** This is for 1.9+ servers only.|
|Name: `attackspeed`<br>Input: `Element(Decimal)`|Sets the base attack speed of this entity.<br>**Related tags:** `<e@entity.armor>`<br>**Note:** This is for 1.9+ servers only.|
|Name: `knockbackresist`<br>Input: `Element(Decimal)`|Sets the base knockback resistance of this entity.<br>**Related tags:** `<e@entity.knockbackresist>`<br>**Note:** This is for 1.9+ servers only.|
|Name: `jointeam`<br>Input: `Element(String)`|Makes this entity join the specified team and leave any other team the entity is in, if any.<br>**Related tags:** `<e@entity.hasteam>`, `<e@entity.team>`|
|Name: `leaveteam`<br>Input: `none`|Makes this entity leave the team they are in, if any.<br>**Related tags:** `<e@entity.hasteam>`, `<e@entity.team>`|
  
----
### dMythicSpawner Mechanisms
|Mechanisms|Descriptions|
|---|---|
|Name: `activate`<br>Input: `Element(Boolean)`|Sets whether this MythicSpawner is active.<br>**Related tags:** `none`|
|Name: `cooldown`<br>Input: `Element(Number)`|Sets the cooldown for this MythicSpawner, in seconds.<br>**Related tags:** `<mythicspawner@mythicspawner.cooldown>`|
|Name: `remainingcooldown`<br>Input: `Element(Number)`|Sets the remaining cooldown time for this MythicSpawner, in seconds.<br>**Related tags:** `<mythicspawner@mythicspawner.remainingcooldown>`|
|Name: `warmup`<br>Input: `Element(Number)`|Sets the warmup for this MythicSpawner, in seconds.<br>**Related tags:** `<mythicspawner@mythicspawner.warmup>`|
|Name: `remainingwarmup`<br>Input: `Element(Number)`|Sets the remaining warmup time for this MythicSpawner, in seconds.<br>**Related tags:** `<mythicspawner@mythicspawner.remainingwarmup>`|
|Name: `mobtype`<br>Input: `Element(String)`|Sets the mobType for this MythicSpawner to a valid MythicMob entity type.<br>**Related tags:** `<mythicspawner@mythicspawner.mobtype>`|
|Name: `moblevel`<br>Input: `Element(Number)`|Sets the level of the entities spawned by this MythicSpawner.<br>**Related tags:** `<mythicspawner@mythicspawner.moblevel>`|
|Name: `spawn`<br>Input: `none`|Forces the MythicSpawner to spawn an entity.<br>**Related tags:** `none`|
|Name: `attach`<br>Input: `dActiveMob`|Attach the specified ActiveMob to this MythicSpawner, if this ActiveMob is not already attached to this MythicSpawner.<br>**Related tags:** `none`|

----
### dTeam Mechanisms
|Mechanisms|Descriptions|
|---|---|
|Name: `addmember`<br>Input: `dEntity`|Adds the specified entity to the team.<br>**Related tags:** `<team@team.members>`, `<e@entity.hasteam>`, `<e@entity.team>`|
|Name: `delmember`<br>Input: `Element(String)`|Removes an entry the team. The `getentitybyentry` command can be used to get the entry name for an entity.<br>**Related tags:** `<team@team.members>`, `<e@entity.hasteam>`, `<e@entity.team>`|
|Name: `displayname`<br>Input: `Element(String)`|Changes the display name of a team.<br>**Related tags:** `<team@team.displayname>`|
|Name: `collision`<br>Input: `Element(String)`|Modifies the value for the COLLISION_RULE option. Valid inputs are "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", and "FOR_OWN_TEAM".<br>**Related tags:** `<team@team.collision>`|
|Name: `deathmessage`<br>Input: `Element(String)`|Modifies the value for the DEATH_MESSAGE_VISIBILITY option. Valid inputs are "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", and "FOR_OWN_TEAM".<br>**Related tags:** `<team@team.deathmessage>`|
|Name: `nametag`<br>Input: `Element(String)`|Modifies the value for the NAME_TAG_VISIBILITY option. Valid inputs are "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", and "FOR_OWN_TEAM".<br>**Related tags:** `<team@team.nametag>`|
|Name: `friendlyinvisibles`<br>Input: `Element(Boolean)`|Sets whether teammates can see each other invisible teammates in the team.<br>**Related tags:** `<team@team.friendlyinvisibles>`|
|Name: `friendlyfire`<br>Input: `Element(Boolean)`|Sets whether teammates can hit each other.<br>**Related tags:** `<team@team.friendlyfire>`|
|Name: `prefix`<br>Input: `Element(String)`|Sets the prefix for this team. Put no input to clear prefix.<br>**Related tags:** `<team@team.prefix>`|
|Name: `suffix`<br>Input: `Element(String)`|Sets the suffix for this team. Put no input to clear suffix.<br>**Related tags:** `<team@team.suffix>`|
