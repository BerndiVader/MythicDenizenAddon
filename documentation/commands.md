# Commands
[Items](#items) <br>
[ActiveMobs](#activemobs) <br>
[Players](#players) <br>
[Skills](#skills)

----
### Items
**Command:** ```getmythicitems (filter:<filter>)``` <br>
**Description:** Grabs a list of MythicItems. <br>
Optionally specify a filter. <br>
**Tags:** `<entry[saveName].mythicitems> `returns a dList of all returned MythicItems. <br>

----
### ActiveMobs
**Command:** `mmapplymythic [entity:<entity>] [mobtype:<element>] (level:<#>)` <br>
**Description:** Transforms an entity into an ActiveMob recognized by MythicMobs. <br>
"`mobtype`" specifies the internal name of the MythicMob that the entity is to be transformed into. <br>
**Tags:** `<entry[saveName].activemob>` returns the dActiveMob of the transformed entity. <br>
**Example:** <br>
*In Denizen:*
```yaml
example_script:
    type: world
    events:
        on player right clicks entity:
        - if !<context.entity.isactivemob> {
            - mmapplymythic entity:<context.entity> mobtype:MythicEntity level:1 save:result
            - mmtrigger activemob:<entry[result].activemob> trigger:SPAWN entity:<context.entity>
            - narrate "Applied MythicMob of MythicEntity to <context.entity>!"
        }
        else {
            - mmremovemythic activemob:<context.entity.activemob>
            - narrate "Removed MythicMob of MythicEntity from <context.entity>!"
        }
```
*In MythicMobs:*
```yaml
MythicEntity:
  Type: zombie
  Skills:
  - message{msg="Me a MythicMobs mob now!"} @world ~onSpawn
```
<br>
<br>

**Command:** `mmremovemythic [activemob:<ActiveMob>]` <br>
**Description:** Transforms an active MythicMob into its vanilla counterpart. <br>
**Tags:** `<entry[saveName].entity>` returns the dEntity of the transformed ActiveMob. <br>
<br>
<br>

**Command:** `mmspawnmob [mobtype:<element>] [<location>] (world:<string>) (level:<#>)` <br>
**Description:** Spawns a MythicMob at a location. <br>
"`mobtype`" specifies the internal name of the MythicMob to be spawned. <br>
**Tags:** `<entry[saveName].activemob>` returns a dActiveMob of the spawned MythicMob. <br>
<br>
<br>

**Command:** `mmcastmob [caster:<ActiveMob>] [target:<entity>/<location>] [skill:<element>] (trigger:<entity>) (power:<#.#>)` <br>
**Description:** Forces an ActiveMob to cast a skill onto a target entity or location. <br>
"`skill`" should be a valid MythicMobs skill. <br>
Optionally specify a trigger entity and power level of the skill. The trigger entity defaults to the caster. The power defaults to "1". <br>
**Tags:** none <br>
<br>
<br>

**Command:** `mmskillcast [caster:<ActiveMob>] [target:<entity>/<location>] [skill:<element>] (trigger:<entity>) (power:<#.#>) (repeat:<#>) (delay:<#>)` <br>
**Description:** Forces an ActiveMob to cast a skill onto a target entity or location. <br>
"`skill`" should be a valid MythicMobs skill. <br>
Optionally specify a trigger entity and power level of the skill. The trigger entity defaults to the caster. The power defaults to "1". <br>
Optionally specify the amount of times the skill should repeat, with an optional delay between each iteration. The delay is in ticks. <br>
**Tags:** none <br>
<br>
<br>

**Command:** `mmsignal [activemob:<ActiveMob>] [singal:<element>] (trigger:<entity>)` <br>
**Description:** Sends a signal to an active MythicMob. <br>
Optionally specify a trigger entity. The trigger entity defaults to the MythicMob receiving the signal. <br>
**Tags:** none <br>
<br>
<br>

**Command:** `mmtrigger [activemob:<ActiveMob>] [trigger:<element>] [entity:<entity>]` <br>
**Description:** Triggers an active MythicMob's trigger, where "entity" is the entity triggering the active MythicMob. <br>
Valid triggers are: <br>
```
DEFAULT, ATTACK, BOW_HIT, BLOCK, COMBAT,
CROUCH, UNCROUCH, DAMAGED, DROPCOMBAT,
DEATH, ENTERCOMBAT, EXPLODE, INTERACT, KILL,
KILLPLAYER, PLAYERDEATH, SHOOT, SIGNAL,
SPAWN, SPLASH_POTION, SWING, TARGETCHANGE,
TELEPORT, TIMER, USE, READY
```
**Tags:** none <br>
**Example:** <br>
*In Denizen:*
```yaml
example_script:
    type: world
    events:
      on entity damaged by projectile:
      - if <context.entity.isactivemob> && <context.projectile.name> == "arrow" {
          - mmtrigger activemob:<context.entity.activemob> trigger:boghit entity:<context.damager>
      }
```
*In MythicMobs:*
```yaml
Monkey:
  Type: zombie
  Display: "MythicMobs Monkey"
  Health: 40
  Armor: 10
  Skills:
  - message{msg="Ouch! Stop shooting arrows at me!"} @trigger ~onBowHit 1
```

----
### Players
**Command:** `mmcastplayer [caster:<entity>] [skill:<element>] [target:<entity>/<location>] (trigger:<entity>) (repeat:<#>) (delay:<#>)` <br>
**Description:** Forces a player to use a MythicMobs skill. <br>
Optionally specify a trigger entity. The trigger entity defaults to the caster. <br>
Optionally specify the amount of times the skill should repeat, with an optional delay between each iteration. The delay is in ticks. <br>
**Tags:** none <br>
**Example:** <br>
*In Denizen:*
```yaml
example_script:
    type: world
    events:
        on player clicks:
        - if <player.target||null> != null {
            - mmcastplayer caster:<player.entity> skill:Damage target:<player.target>
        }
        else {
            - mmcastplayer caster:<player.entity> skill:Heal target:<player.entity> repeat:4 delay:20
        }
```
*In MythicMobs:*
```yaml
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
```

---
### Skills
**Command:** `getmythicskills (filter:<regex>) (strict:<boolean>)` <br>
**Description:** Returns a list of all registered MythicMobs skills. <br>
Optionally specify a filter to limit the output. <br>
Optionally specify strict true to get a single skill. This requires filter to be set to the skillname! <br>
**Tags:** `<entry.[saveName].skills>` A list of skills (if strict=false) <br>
`<entry.[saveName].skill>` a dMythicSkill object (if strict=true)
