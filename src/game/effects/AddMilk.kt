package game.effects

import game.Character
import game.Effect
import game.Game

class AddMilk: Effect {

    override var probability: Double
    val player: Character

    constructor(probability: Double, player: Character){
        this.probability = probability
        this.player = player
    }

    constructor(map: Map<String, Any>, game: Game){
        probability = map[GlobalEffectFactory.PROBABLITY_NAME] as Double
        player = game.characterById(map["PLAYER"] as Int)
    }

    override fun equals(other: Any?): Boolean {
        if(other is AddMilk){
            return true
        }
        return false
    }
    override fun apply(game: Game) {
        game.hasMilk.add(player)
        player.dummyScore -= 0.45
    }
    override fun toString(): String{
        return "get milk for $player with probability $probability"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalEffectFactory.TYPE_NAME to GlobalEffectFactory.ADD_MILK_NAME,
            GlobalEffectFactory.PROBABLITY_NAME to probability,
            "PLAYER" to player.id
        )
    }

    override fun describe(): String {
        return "${player} would get milk"
    }
}