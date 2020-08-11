package shortstate.linetriggers

import game.Game
import scenario.TRIGGER_MAP
import shortstate.ShortStateCharacter
import shortstate.dialog.Line

class LineTrigger {

    companion object{
        fun triggerFromSaveString(saveString: Map<String, Any>): LineTrigger{
            val retval = TRIGGER_MAP[saveString["type"] as String]!!
            retval.data = (saveString["data"] as Map<String, Any>).toMutableMap()
            return retval
        }
    }

    val id: String
    private val condition: (data: MutableMap<String, Any>, game: Game, line: Line?,  me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean
    private val line: (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line
    var data: MutableMap<String, Any>

    constructor(id: String, condition: (data: MutableMap<String, Any>,
                            game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean,
                line: (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line): this(id, mutableMapOf<String, Any>("calls" to 0), condition, line){
    }

    constructor(id: String, data: MutableMap<String, Any>, condition: (data: MutableMap<String, Any>,
                                                           game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean,
                line: (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line){
        this.id = id
        this.data = data
        this.condition = condition
        this.line = line
    }

    fun shouldGenerateLine(game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?): Boolean{
        try{
           generateLine(game, line, me, other) //if generating the line would crash, the condition fails silently.
           return condition(this.data, game, line, me, other)
        } catch(e: Exception){
            return false
        }
    }

    fun generateLine(game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?): Line {
        val retval = line(this.data, game, line, me, other)
        retval.source = this
        return retval
    }

    fun saveString(): Map<String, Any>{
        return mapOf("type" to id, "data" to data)
    }
}