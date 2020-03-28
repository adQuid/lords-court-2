package game.action

import game.GameCharacter
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime

object GlobalActionTypeFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Action> = hashMapOf(
        "WasteTime" to {map -> WasteTime()},
        "BakeCookies" to {map -> BakeCookies()},
        "GetMilk" to {map -> GetMilk(map["target"] as GameCharacter) }
    )

    fun fromMap(map: Map<String, Any>): Action {
        return typeMap[map[TYPE_NAME]]!!(map)
    }

}