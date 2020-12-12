package game

import game.action.Action
import game.action.ActionMemory
import shortstate.dialog.Line
import shortstate.dialog.LineMemory

class Memory {

    val LINES_NAME = "LINES"
    val lines: MutableList<LineMemory>
    val ACTIONS_NAME = "ACTIONS"
    val comittedActions: MutableList<ActionMemory>

    constructor(){
        lines = mutableListOf()
        comittedActions = mutableListOf()
    }

    constructor(other: Memory){
        lines = other.lines.map{ memory -> LineMemory(memory)}.toMutableList()
        comittedActions = other.comittedActions.map { memory -> ActionMemory(memory) }.toMutableList()
    }

    constructor(saveString: Map<String, Any>, game: Game){
        lines = (saveString[LINES_NAME] as List<Map<String, Any>>).map { map -> LineMemory(map, game)}.toMutableList()
        comittedActions = (saveString[ACTIONS_NAME] as List<Map<String, Any>>).map { map -> ActionMemory(map, game)}.toMutableList()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            LINES_NAME to lines.map { memory -> memory.saveString() },
            ACTIONS_NAME to comittedActions.map{ memory -> memory.saveString() }
        )
    }

    fun canRememberSaying(line: Line): Boolean{
        return lines.filter { it.line == line }.isNotEmpty()
    }

}