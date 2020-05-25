package gamelogicmodules.cookieworld.actionTypes

import game.action.Action
import game.Effect
import game.effects.AddMilk
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import game.titles.Milkman

class GetMilk: Action {

    companion object{
        val typeName: String
            get() = "GetMilk"
    }
    val player: GameCharacter

    constructor(player: GameCharacter){
        this.player = player
    }

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        if(player.titles.filter { title -> title is Milkman }.isNotEmpty()) {
            return listOf(AddMilk(1.0, this.player))
        }
        return listOf()
    }

    override fun toString(): String {
        return "get milk for ${player.name}"
    }

    override fun equals(other: Any?): Boolean {
        return other is GetMilk
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "target" to player.id
        )
    }
}