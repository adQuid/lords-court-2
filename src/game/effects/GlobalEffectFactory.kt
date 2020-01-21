package game.effects

import game.Effect
import game.Game

object GlobalEffectFactory {

    val ADD_DELICIOUSNESS_NAME = "addDel"
    val ADD_MILK_NAME = "addMlk"

    val PROBABLITY_NAME = "PROBABILITY"
    val TYPE_NAME = "type"
    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Effect> = hashMapOf(
        ADD_DELICIOUSNESS_NAME to {map, game -> AddDelicousness(map, game)},
        ADD_MILK_NAME to {map, game -> AddMilk(map, game)}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Effect {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }
}