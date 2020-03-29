package game.action

import game.Game
import game.GameCharacter
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime

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