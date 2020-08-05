package game.linetriggers

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.Line

class LineTrigger {

    val id: String
    private val condition: (data: MutableMap<String, Any>, game: ShortStateGame, line: Line,  me: ShortStateCharacter) -> Boolean
    private val line: (data: MutableMap<String, Any>, game: ShortStateGame, line: Line, me: ShortStateCharacter) -> Line
    var data: MutableMap<String, Any>

    constructor(id: String, condition: (data: MutableMap<String, Any>,
                            game: ShortStateGame, line: Line, me: ShortStateCharacter) -> Boolean,
                line: (data: MutableMap<String, Any>, game: ShortStateGame, line: Line, me: ShortStateCharacter) -> Line): this(id, mutableMapOf<String, Any>(), condition, line){
    }

    constructor(id: String, data: MutableMap<String, Any>, condition: (data: MutableMap<String, Any>,
                                                           game: ShortStateGame, line: Line, me: ShortStateCharacter) -> Boolean,
                line: (data: MutableMap<String, Any>, game: ShortStateGame, line: Line, me: ShortStateCharacter) -> Line){
        this.id = id
        this.data = data
        this.condition = condition
        this.line = line
    }

    fun shouldGenerateLine(game: ShortStateGame, line: Line, me: ShortStateCharacter): Boolean{
        return condition(this.data, game, line, me)
    }

    fun generateLine(game: ShortStateGame, line: Line, me: ShortStateCharacter): Line {
        return line(this.data, game, line, me)
    }

    fun saveString(): Map<String, Any>{
        return mapOf("type" to id, "data" to data)
    }
}