# Examples

----
### dscript mechanic & createmythicmeta & getmythicskill
*In Denizen:*
```yaml
dscript_example:
	type: task 
	speed: 0 
	debug: true
	
	script:
	- createmythicmeta cause:api caster:<context.data.caster> trigger:<context.data.trigger> origin:<context.data.origin> targets:li@<context.data.caster> power:1 save:meta
	- getmythicskills filter:msg strict:true save:mythic
	- if <entry[mythic].skill.execute[<entry[meta].mythicmeta>]> {
		- announce "true"
	} else {
		- announce "faLSE"
  }
```
*In MythicMobs:*
```yaml
DenizenMonkey:
  Type: zombie
  Display: "DenizenMonkey"
  Health: 20
  Skills:
  - equip{i=BlackbeardHead:4} @self ~onSpawn
  - dscript{script=dscript_example;example=hihihi;other=hohoho;meme=hihihi} @trigger ~onDamaged
```
