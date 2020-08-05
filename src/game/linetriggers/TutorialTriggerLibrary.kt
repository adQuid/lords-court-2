package game.linetriggers

import shortstate.dialog.linetypes.SimpleLine

val testTrigger = LineTrigger("test", { data, game, line, me ->
    if(me.player.memory.lines.size < 2){ true } else { false }
},
    {data, game, line, me -> SimpleLine("test") }
)

val TRIGGER_MAP = mutableMapOf(testTrigger.id to testTrigger)

fun triggerFromSaveString(saveString: Map<String, Any>): LineTrigger{
    val retval = TRIGGER_MAP[saveString["type"] as String]!!
    retval.data = (saveString["data"] as Map<String, Any>).toMutableMap()
    return retval
}