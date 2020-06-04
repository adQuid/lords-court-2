package gamelogicmodules.cookieworld.actionTypes

import game.action.Action
import game.Effect
import game.effects.AddMilk
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import game.titles.Milkman
import javafx.scene.Scene
import shortstate.ShortStateCharacter

class GetMilk: Action {

    companion object{
        val typeName: String
            get() = "GetMilk"
    }
    val player: Int

    constructor(player: GameCharacter){
        this.player = player.id
    }

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        if(player.titles.filter { title -> title is Milkman }.isNotEmpty()) {
            return listOf(AddMilk(1.0, game.characterById(this.player)!!))
        }
        return listOf()
    }

    override fun toString(): String {
        return "get milk for ${player}"
    }

    override fun description(): String {
        return "Give ${player} milk. A character with milk gets points from the game's deliciousness level."
    }

    override fun equals(other: Any?): Boolean {
        if(other is GetMilk){
            return this.player == other.player
        }
        return false
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "target" to player
        )
    }
}