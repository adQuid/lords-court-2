package gamelogic.cookieworld.effects

import game.GameCharacter
import game.Effect
import game.Game
import gamelogic.cookieworld.CookieWorld

class AddMilk: Effect {

    companion object{
        val typeName = "addMilk"
    }

    override var probability: Double
    val player: GameCharacter

    constructor(probability: Double, player: GameCharacter){
        this.probability = probability
        this.player = player
    }

    constructor(map: Map<String, Any>, game: Game){
        probability = map[CookieWorldEffectFactory.PROBABLITY_NAME] as Double
        player = game.characterById(map["PLAYER"] as Int)
    }

    override fun equals(other: Any?): Boolean {
        if(other is AddMilk){
            return true
        }
        return false
    }
    override fun apply(game: Game) {
        val logic = CookieWorld.getCookieWorld(game)
        logic.hasMilk.add(player)
        player.dummyScore -= 0.45
    }
    override fun toString(): String{
        return "get milk for $player with probability $probability"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldEffectFactory.TYPE_NAME to typeName,
            CookieWorldEffectFactory.PROBABLITY_NAME to probability,
            "PLAYER" to player.id
        )
    }

    override fun describe(): String {
        return "${player} would get milk"
    }
}