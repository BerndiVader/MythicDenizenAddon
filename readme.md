# Denizen Support for MythicMobs 4.x.x


#### Conditions for dObject entity:
##### <entity.isactivemob> - Returns true if activemob or false if not.
##### <entity.activemob> - Returns the activemob instance of the entity.

#### Conditions for dObject activemob:
##### <activemob.isdead> - True or False if activemob is dead.
##### <activemob.hasthreattable> - True of False if activemob has target threattable.
##### <activemob.hasmythicspawner> - Returns true if the activemob has a MythicSpawner.

#### Expressions for dObject activemob:
##### <activemob.mobtype> - Returns the name of the MobType of the ActiveMob.
##### <activemob.displayname> - Returns the DisplayName of the ActiveMob.

#### Adjustments for dObject activemob:
##### adjust <activemob> remove - Removes & unregister the ActiveMob from MythicMobs.