package gamelogic.cookieworld.actionTypes

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import game.titles.Milkman
import gamelogic.cookieworld.CookieWorld
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

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return true
    }

    override fun doAction(game: Game, player: GameCharacter){
        if(player.titles.filter { title -> title is Milkman }.isNotEmpty()) {
            val logic = CookieWorld.getCookieWorld(game)
            logic.hasMilk.add(game.characterById(this.player))
            player.dummyScore -= 0.45
        }
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
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

    override fun collidesWith(other: Action): Boolean {
        return other is GetMilk && other.player == player
    }
}