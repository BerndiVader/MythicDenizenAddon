# Denizen Support for MythicMobs 4.9.0

# [DOWNLOAD](http://mc.hackerzlair.org:8080/job/MythicDenizenAddon/) [![Build Status](http://mc.hackerzlair.org:8080/job/MythicDenizenAddon/badge/icon)] <br>

- Update 0.602	- MythicMobs 4.9.0 support
- Update 0.601	- Denizen 1.1.2, MythicMobs 4.7.2 && Spigot 1.15.2 support
				- fix for issue with events on reload
				- fix for bukkit entity not avail in mythicmobsdeath event
				- removed currency & xp from mythicmobsdeathevent, use mythicmobslootdrop event instead
- Update 0.600a - Denizen 1.10, MythicMobs 4.6.5 && Spigot 1.14.4
- Update 0.515d - added dMythicMechanic object
				- added getmythicmechanic command
- Update 0.515c - added castmythicskill command
- Update 0.515b - added added spawn to dMythicMob object
				- added dentityscript mythicmobs targeter
				- added dlocationscript mythicmobs targeter
- Update 0.515a - added dentity mythicmobs script mechanic
				- added dlocation mythicmobs script mechanic
				- added dentitycompare mythicmobs script mechanic
				- added dlocationcompare mythicmobs script mechanic
				- fixed npe inside <mythicskill@skill.execute[data]>
				  if casting entity is dead.
- Update 0.513c - updated firequestobjective command
- Update 0.513b - added firequestobjective command
- Update 0.513a - renamed scriptcall for drop & mechanic
                  both now called dscript or denizenscript
                - added createmythicmeta command
                - added documentation for dMythicMeta & dMythicSkill object
- Update 0.512a - fix for cached scriptcontainer
				- added dskill mechanic
			    - added dMythicMeta object
			    - added dMythicSkill object
- Update 0.511a - added support for mythicmobs 4.5.7
				  added dscriptdrop 
- Update 0.510a - added support for mythicmobs 4.5.1 and denizen 1.0.3
- Update 0.501a - added mythicmobs entitytargeter && mythicmobs locationtargeter event
- Update 0.500c - up to Denizen 1.0.2-r06 again.
                  added `<context.money>` && `<context.exp>` to mythicmobs lootdrop event
- Update 0.500b - gone back to denizen 1.0.2-SNAPSHOT (build 1649), core version: 1.15 (Build 108)
                  because the devbuilds are not compatible with the official release at spigot resource page.
                - proper handling of dList
                - fixed droptable drops didnt work
- Update 0.500a - added support for MythicMobs 4.4.0.
                - added mythicmobs lootdrop event
                - added support for denizen 1.0.2-SNAPSHOT (build 1651), core version: 1.15 (Build 152)
- Update 0.499d - added sort_by_distance to dList
- Update 0.499b - added dMythicSkill object, added getmythicskills command
- Update 0.499a - added MythicItem dObject, added getmythicitems command
- Update 0.499  - even more rework
- Update 0.498  - more rework
<br>
<br>

**General Information** <br>
[Events](documentation/events.md) <br>
[Tags](documentation/tags.md) <br>
[Mechanisms](documentation/mechanisms.md) <br>
[Commands](documentation/commands.md) <br>
[Drops](documentation/drops.md)<br>
[MythicMobs Mechanics / Conditions / Drops / Targeters](documentation/mythicmechanics.md)<br><br>


[Examples](documentation/examples.md)

**dObjects**<br>
\- dActiveMob<br>
\- dMythicSpawner<br>
\- dMythicMob<br>
\- dMythicItem<br>
\- dMythicMeta<br>
\- dMythicSkill<br>
\- dMythicMechanic<br>
\- dTeam<br>
\- dListExt<br>
