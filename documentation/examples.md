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

----
### firecustomobjective command for Quests & MythicMobsQuestModule

*In Denizen:*
```yaml
Events:
  type: world
  debug: false
  
  events:

    on player consumes item:
      - announce <context.item.material>
      - firequestobjective action:INCREMENT quester:<player> "type:consume <context.item.material>"
```

*In Quests:*
```yaml
Quests:
  custom1:
    name: 'The booze battle'
    ask-message: 'You booze more than the old drunkard?'
    finish-message: 'Well done! You are far gone, but you are now the new drunkard in town!'
    stages:
      ordered:
        '1':
          custom-objectives:
            custom1:
              name: Custom Denizen
              count: 5
              data:
                Objective Name: Defeat the old drunkard!
                Objective Type: consume m@potion
                Notify player: 'true'
                Notify Message: '%c% %s%'
```
