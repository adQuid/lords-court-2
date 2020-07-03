package gamelogicmodules.capital

import game.Game
import game.Location
import game.Title
import gamelogicmodules.resources.Resources
import gamelogicmodules.territory.Territory
import gamelogicmodules.territory.TerritoryLogicModule

class Capital {

    val resources: Resources
    val terId: Int
    var locationId: Int
    var territory: Territory?
    var location: Location?
    val taxes: MutableMap<String, Double>

    constructor(territory: Territory){
        resources = Resources()
        this.territory = territory
        terId = territory.id
        location = Location(territory.x, territory.y)
        locationId = location!!.id
        taxes = mutableMapOf(Territory.FLOUR_NAME to 0.1)
    }

    constructor(other: Capital){
        resources = Resources(other.resources)
        territory = null //this will be overwritten in finishConstruction
        terId = other.territory!!.id
        locationId = other.locationId
        location = null
        taxes = other.taxes.toMutableMap()
    }

    constructor(saveString: Map<String, Any>){
        resources = Resources(saveString["res"] as Map<String, Any>)
        territory = null //this will be overwritten in finishConstruction
        terId = saveString["ter"] as Int
        locationId = saveString["loc"] as Int
        location = null
        taxes = (saveString["tax"] as Map<String, Double>).toMutableMap()
    }

    fun finishConstruction(game: Game){
        territory = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terId)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "res" to resources.saveString(),
            "ter" to territory!!.id,
            "loc" to locationId,
            "tax" to taxes
        )
    }

    fun generateCountTitle(): Title {
        return Count(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Capital

        if (terId != other.terId) return false

        return true
    }

    override fun hashCode(): Int {
        return terId
    }
}