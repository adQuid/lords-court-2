package game.action

import game.Character
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime

object GlobalActionTypeFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Action.ActionType> = hashMapOf(
        "WasteTime" to {map -> WasteTime()},
        "BakeCookies" to {map -> BakeCookies()},
        "GetMilk" to {map -> GetMilk(map["target"] as Character) }
    )

    fun fromMap(map: Map<String, Any>): Action.ActionType {
        return typeMap[map[TYPE_NAME]]!!(map)
    }

}