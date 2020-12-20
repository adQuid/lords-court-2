package gamelogic.petitioners

import com.beust.klaxon.Klaxon
import game.Game
import game.GameCharacter
import game.action.Action
import shortstate.ShortStateCharacter
import java.io.File

class SpecialAction: Action {

    companion object{
        const val typeName = "special"
        var allTypes = listOf<SpecialAction>()

        fun loadActionTypes(){
            val klac = Klaxon()
            val loadMap = klac.parseArray<Map<String,Any>>(File("data/special_actions.txt").readText())!!
            allTypes = loadMap.map{ SpecialAction(it) }
        }
    }

    val name: String

    constructor(definition: Map<String, Any>){
        name = definition["name"] as String
    }

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return true
    }

    override fun doAction(game: Game, player: GameCharacter) {

    }

    override fun saveString(): Map<String, Any> {
        return mapOf(
            "name" to name
        )
    }

    override fun collidesWith(other: Action): Boolean {
        return false
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return name
    }

    override fun description(): String {
        return name
    }
}