package game.action

import game.Game
import gamelogicmodules.cookieworld.actionTypes.BakeCookies
import gamelogicmodules.cookieworld.actionTypes.GetMilk
import gamelogicmodules.cookieworld.actionTypes.WasteTime

object GlobalActionTypeFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>, Game) -> Action> = hashMapOf(
        "WasteTime" to {map, game -> WasteTime()},
        "BakeCookies" to {map, game -> BakeCookies()},
        "GetMilk" to {map, game -> GetMilk(game.characterById(map["target"] as Int)) }
    )

    fun fromMap(map: Map<String, Any>, game: Game): Action {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }

}