package game.gamelogicmodules.capital

import game.Game
import game.gamelogicmodules.resources.Resources
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule

class Capital {

    val resources: Resources
    val terId: Int
    var territory: Territory?
    val taxes: MutableMap<String, Double>

    constructor(territory: Territory){
        resources = Resources()
        this.territory = territory
        terId = territory.id
        taxes = mutableMapOf(Territory.FLOUR_NAME to 0.1)
    }

    constructor(other: Capital){
        resources = Resources(other.resources)
        territory = null //this will be overwritten in finishConstruction
        terId = other.territory!!.id
        taxes = other.taxes.toMutableMap()
    }

    constructor(saveString: Map<String, Any>){
        resources = Resources(saveString["res"] as Map<String, Any>)
        territory = null //this will be overwritten in finishConstruction
        terId = saveString["ter"] as Int
        taxes = (saveString["tax"] as Map<String, Double>).toMutableMap()
    }

    fun finishConstruction(game: Game){
        territory = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terId)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "res" to resources.saveString(),
            "ter" to territory!!.id,
            "tax" to taxes
        )
    }
}