package gamelogicmodules.territory.effects

import game.Effect
import game.Game

object TerritoryEffectFactory {

    val PROBABLITY_NAME = "PROBABILITY"
    val TYPE_NAME = "type"
    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Effect> = hashMapOf(
        Starvation.typeName to {map, game ->  Starvation(map, game)}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Effect {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }
}