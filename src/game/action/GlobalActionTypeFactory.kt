package game.action

import game.Game
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.cookieworld.actionTypes.BakeCookies
import gamelogic.cookieworld.actionTypes.GetMilk
import gamelogic.cookieworld.actionTypes.WasteTime
import gamelogic.government.actionTypes.GiveTerritory
import gamelogic.playerresources.GiveResource

object GlobalActionTypeFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>, Game) -> Action> = hashMapOf(
        WasteTime.typeName to {map, game -> WasteTime()},
        BakeCookies.typeName to {map, game -> BakeCookies()},
        GetMilk.typeName to {map, game -> GetMilk(game.characterById(map["target"] as Int)) },
        SetTaxRate.typeName to {map, game -> SetTaxRate(map["territory"] as Int, map["amount"] as Double)},
        GiveTerritory.typeName to {map, game -> GiveTerritory(map["territory"] as Int, map["target"] as Int)},
        GiveResource.typeName to {map, game -> GiveResource(map["char"] as Int, map["resource"] as String, map["amount"] as Int)}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Action {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }

}