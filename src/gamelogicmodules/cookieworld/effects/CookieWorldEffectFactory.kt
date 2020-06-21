package gamelogicmodules.cookieworld.effects

import game.Effect
import game.Game

object CookieWorldEffectFactory {

    val PROBABLITY_NAME = "PROBABILITY"
    val TYPE_NAME = "type"
    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Effect> = hashMapOf(
        AddDelicousness.typeName to {map, game -> AddDelicousness(map, game)},
        AddMilk.typeName to {map, game -> AddMilk(map, game)}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Effect {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }
}