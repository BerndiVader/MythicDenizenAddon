# Drops
[dscriptdrop](#dscriptdrop) <br>

----
### dscriptdrop
**Drop:** `dscriptdrop{script=scriptname;[attributes(optional)]}` <br>
**Description:** Execute a denizen script at drop. <br>
Optionally specify some attributes. <br>
**Tags:** 
`<context.dropper> `entity of the dropper <br>
`<context.cause> `entity of the cause <br>
`<context.player> `player if present <br>
`<context.amount> `amount <br>
**Example:**
denizen script:

```
flubb:
  type: task 
  speed: 0 
  debug: false
  script:
    - announce "<def[some]> <def[example]> <def[attributes]>"
    - announce <context.dropper>
    - announce <context.cause>
    - announce <context.player>
    - announce <context.amount>
```
MythicMobs mob:

```
example_mob:
  Type: zombie
  Health: 10
  AITargetSelectors:
  - 0 clear
  Options:
    PreventOtherDrops: true
  Drops:
  - dscriptdrop{script=flubb;some=hihi;example=hoho;attributes=juhu}
```
