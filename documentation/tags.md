# Tags/Attributes
[World tags](#dworld-tags) <br>
[Entity tags](#dentity-tags) <br>
[ActiveMob tags](#dactivemob-tags) <br>
[MythicItem tags](#dmythicitem-tags) <br>
[MythicSkill tags](#dmythicskill-tags) <br>
[MythicMeta tags](#dmythicmeta-tags) <br>
[MythicSpawner tags](#dmythicspawner-tags) <br>
[Team tags](#dteam-tags) <br>
[List tags](#dlist-tags) <br>
[Tag examples](#tag-examples)

----
### dWorld Tags
|Tag Names|Descriptions|
|---|---|
|`<w@world.allactivemobs>`<br>Returns: dList(dActiveMob)|Returns a list of all ActiveMobs in this world.|
|`<w@world.allmythicspawners>`<br>Returns: dList(dMythicSpawner)|Returns a list of all MythicSpawners in this world.|

----
### dEntity Tags
|Tag Names|Descriptions|
|---|---|
|`<e@entity.isactivemob>`<br>Returns: Element(Boolean)|Returns whether this entity is an ActiveMob.|
|`<e@entity.activemob>`<br>Returns: dActiveMob|Returns the ActiveMob of this entity, if any.|
|`<e@entity.damage>`<br>Returns: Element(Decimal)|Returns the base damage of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.followrange>`<br>Returns: Element(Decimal)|Returns the base following range of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.armor>`<br>Returns: Element(Decimal)|Returns the base armor of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.attackspeed>`<br>Returns: Element(Decimal)|Returns the base attack speed of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.knockbackresist>`<br>Returns: Element(Decimal)|Returns the base knockback resistance of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.jumpstrength>`<br>Returns: Element(Decimal)|Returns the base jump strength of this entity.<br>NOTE: This is for 1.9+ servers only.|
|`<e@entity.maxnodamageticks>`<br>Returns: Element(Decimal)|Returns the maximum amount of ticks in which this entity does not take damage after initially taking damage.|
|`<e@entity.nodamageticks>`<br>Returns: Element(Decimal)|Returns the amount of ticks left in which this entity does not take damage.|
|`<e@entity.mmtargets[<element>]>`<br>Returns: dList(dEntity/dLocation)|Returns a list of targets as if this entity is using a MythicMobs targeter.<br>[Click this to see an example](#mmtargets)|
|`<e@entity.hasteam>`<br>Returns: Element(Boolean)|Returns whether this entity has a scoreboard team.|
|`<e@entity.team>`<br>Returns: dTeam|Returns the scoreboard team this entity is in, if any.|

----
### dActiveMob Tags
|Tag Names|Descriptions|
|---|---|
|`<activemob@activemob.isdead>`<br>Returns: Element(Boolean)|Returns whether this ActiveMob is dead.|
|`<activemob@activemob.hasthreattable>`<br>Returns: Element(Boolean)|Returns whether this ActiveMob has a target ThreatTable.|
|`<activemob@activemob.hasmythicspawner>`<br>Returns: Element(Boolean)|Returns whether this ActiveMob has a MythicSpawner.|
|`<activemob@activemob.hastarget>`<br>Returns: Element(Boolean)|Returns whether this ActiveMob has a target.|
|`<activemob@activemob.mobtype>`<br>Returns: Element(String)|Returns the entity type of this ActiveMob.|
|`<activemob@activemob.displayname>`<br>Returns: |Returns the custom name of this ActiveMob, if any.|
|`<activemob@activemob.location>`<br>Returns: dLocation|Returns the location of this ActiveMob.|
|`<activemob@activemob.world>`<br>Returns: dWorld|Returns the world this ActiveMob is in.|
|`<activemob@activemob.owner>`<br>Returns: dEntity|Returns the dEntity of the owner of this ActiveMob.|
|`<activemob@activemob.lastaggro`<br>Returns: dEntity|Returns the last dEntity to aggro this ActiveMob.|
|`<activemob@activemob.toptarget>`<br>Returns: Element(String)/dEntity|Returns the top target of this ActiveMob's ThreatTable, or the target of the ActiveMob as a dEntity.|
|`<activemob@activemob.uuid>`<br>Returns: Element(String)|Returns the UUID of this ActiveMob.|
|`<activemob@activemob.health>`<br>Returns: Element(Number)|Returns how much health this ActiveMob has as an integer.|
|`<activemob@activemob.maxhealth>`<br>Returns: Element(Number)|Returns the maximum health for this ActiveMob as an integer.|
|`<activemob@activemob.faction>`<br>Returns: Element(String)|Returns the faction of this ActiveMob.|
|`<activemob@activemob.stance>`<br>Returns: Element(String)|Returns the stance of this ActiveMob.|
|`<activemob@activemob.level>`<br>Returns: Element(Number)|Returns the level of this ActiveMob.|
|`<activemob@activemob.playerkills>`<br>Returns: Element(Number)|Returns the number of players killed by this ActiveMob.|
|`<activemob@activemob.lastsignal>`<br>Returns: Element(String)|Returns the last signal this ActiveMob received, if any.|
|`<activemob@activemob.mythicspawner>`<br>Returns: dMythicSpawner|Returns the MythicSpawner of this ActiveMob, if any.|
|`<activemob@activemob.threattable>`<br>Returns: dList(dEntity)|Returns a list of entities in this ActiveMob's ThreatTable.|
|`<activemob@activemob.threatvalueof[<entity>]>`<br>Returns: Element(Decimal)|Returns the threat value of the specified entity, according to this ActiveMob's ThreatTable.|
|`<activemob@activemob.damage>`<br>Returns: Element(Decimal)|Returns the maximum amount of damage this ActiveMob can take.|
|`<activemob@activemob.power>`<br>Returns: Element(Decimal)|Returns the power level of this ActiveMob.|
|`<activemob@activemob.lastdamagaeskillamount>`<br>Returns: Element(Decimal)|Returns the amount of damage from the last used skill of this ActiveMob.|
|`<activemob@activemob.hasimmunitytable>`<br>Returns: Element(Boolean)|Returns whether this ActiveMob has an ImmunityTable enabled.|
|`<activemob@activemob.isonimmunitycooldown[<entity>]>`<br>Returns: Element(Boolean)|Returns whether the specified entity is on this ActiveMob's ImmunityTable.|

----
### dMythicItem Tags
|Tag Names|Descriptions|
|---|---|
|`<mythicitem@mythicitem.type>`<br>Returns: Element(String)|Returns the internal, unique, name for this MythicItem.|
|`<mythicitem@mythicitem.amount>`<br>Returns: Element(Number)|Returns the amount of this MythicItem.|
|`<mythicitem@mythicitem.displayname>`<br>Returns: Element(String)|Returns the display name of this MythicItem.|
|`<mythicitem@mythicitem.lore>`<br>Returns: dList(Element(String))|Returns this MythicItem's lore as a list.|
|`<mythicitem@mythicitem.materialname>`<br>Returns: Element(String)|Returns the name of this MythicItem's material.|
|`<mythicitem@mythicitem.materialdata>`<br>Returns: Element(Number)|Returns the data value of this MythicItem's material.|
|`<mythicitem@mythicitem.get_item>`<br>Returns: dItem|Returns this MythicItem as a dItem.|

----
### dMythicSkill Tags
|Tag Names|Descriptions|
|---|---|
|`<mythicskill@mythicskill.type>`<br>Returns: Element(String)|Returns the internal, unique, name for this MythicSkill.|
|`<mythicskill@mythicskill.present>`<br>Returns: Element(Boolean)|Returns if there is a MythicMobs skill present.|
|`<mythicskill@mythicskill.is_useable([dMythicMeta])>`<br>Returns: Element(Boolean)|Returns if the skill is useable with the stored data, or optional supply new data.|
|`<mythicskill@mythicskill.execute([dMythicMeta])`<br>Returns: Element(Boolean)|cast the skill with the stored data or, optional supply new data on the fly.|

----
### dMythicMeta Tags
|Tag Names|Descriptions|
|---|---|
|`<mythicmeta@mythicmeta.caster>`<br>Returns: Element(dEntity)|Returns the entity used as caster.|
|`<mythicmeta@mythicmeta.cause>`<br>Returns: Element(String)|Returns the cause used to trigger.|
|`<mythicmeta@mythicmeta.trigger>`<br>Returns: Element(dEntity)|Returns the entity used as trigger.|
|`<mythicmeta@mythicmeta.origin>`<br>Returns: Element(dLocation)|Returns the origin location.|
|`<mythicmeta@mythicmeta.targets>`<br>Returns: Element(dList)|Returns a list of the targets.|
|`<mythicmeta@mythicmeta.power>`<br>Returns: Element(Float)|Returns the power.|

----
### dMythicSpawner Tags
|Tag Names|Descriptions|
|---|---|
|`<mythicspawner@mythicspawner.location>`<br>Returns: dLocation|Returns the location of this MythicSpawner.|
|`<mythicspawner@mythicspawner.world>`<br>Returns: dWorld|Returns the world this MythicSpawner is in.|
|`<mythicspawner@mythicspawner.allactivemobs>`<br>Returns: dList(dActiveMob)|Returns a list of all ActiveMobs spawned from this MythicSpawner.|
|`<mythicspawner@mythicspawner.mobtype>`<br>Returns: Element(String)|Returns the entity type of the entities spawned by this MythicSpawner.|
|`<mythicspawner@mythicspawner.moblevel>`<br>Returns: Element(Number)|Returns the level of the entities spawned by this MythicSpawner.|
|`<mythicspawner@mythicspawner.cooldown>`<br>Returns: Element(Number)|Returns the cooldown time of this MythicSpawner, in seconds.|
|`<mythicspawner@mythicspawner.remainingcooldown>`<br>Returns: Element(Number)|Returns the remaining seconds left for this MythicSpawner's cooldown.|
|`<mythicspawner@mythicspawner.warmup>`<br>Returns: Element(Number)|Returns the warmup time for this MythicSpawner, in seconds.|
|`<mythicspawner@mythicspawner.remainingwarmup>`<br>Returns: Element(Number)|Returns the remaining seconds left for this MythicSpawner's warmup.|
|`<mythicspawner@mythicspawner.mobamount>`<br>Returns: Element(Number)|Returns the amount of spawned and living ActiveMobs from this MythicSpawner.|
|`<mythicspawner@mythicspawner.maxmobamount>`<br>Returns: Element(Number)|Returns the maximum amount of entities this MythicSpawner can spawn.|

----
### dTeam Tags
|Tag Names|Descriptions|
|---|---|
|`<team@team.name>`<br>Returns: Element(String)|Returns the internal name of this team.|
|`<team@team.displayname>`<br>Returns: Element(String)|Returns the display name of this team.|
|`<team@team.members>`<br>Returns: dList(Element(String))|Returns a list of the members of the team. The UUIDs of entities and the usernames of players will be returned.|
|`<team@team.collision>`<br>Returns: Element(String)|Returns the value of the COLLISION_RULE option of this team. Can be "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", or "FOR_OWN_TEAM".|
|`<team@team.deathmessage>`<br>Returns: Element(String)|Returns the value of the DEATH_MESSAGE_VISIBILITY option of this team. Can be "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", or "FOR_OWN_TEAM".|
|`<team@team.nametag>`<br>Returns: Element(String)|Returns the value of the NAME_TAG_VISIBILITY option of this team. Can be "ALWAYS", "NEVER", "FOR_OTHER_TEAMS", or "FOR_OWN_TEAM".|
|`<team@team.friendlyfire>`<br>Returns: Element(Boolean)|Returns whether this team has friendly fire enabled.|
|`<team@team.friendlyinvisibles>`<br>Returns: Element(Boolean)|Returns whether members of this team can see other invisible members of this team.|
|`<team@team.prefix>`<br>Returns: Element(String)|Returns the prefix of this team.|
|`<team@team.suffix>`<br>Returns: Element(String)|Returns the suffix of this team.|

---
### dList Tags
|Tag Names|Descriptions|
|---|---|
|`<li@(dentity)(dlocation).sort_by_distance[(dEntity)(dLocation)]>`<br>Returns: Element(dList)|Returns sorted list.|

----
## Tag Examples
#### mmtargets
```yaml
# Returns a list of entities
# (dList(dEntity))
- announce <player.entity.mmtargets[@EIR{r=30}]>

# Returns a list of locations
# (dList(dLocation))
- announce <player.entity.mmtargets[@Ring{r=10;p=5}]>
```
