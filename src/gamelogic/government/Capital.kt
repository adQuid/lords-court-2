package gamelogic.government

import game.Game
import game.Location
import game.Title
import gamelogic.government.laws.GlobalLawFactory
import gamelogic.resources.Resources
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule

class Capital {

    val resources: Resources
    val terId: Int
    var territory: Territory?
    var location: Location

    private val laws: MutableSet<Law>
    val taxes: MutableMap<String, Double>

    constructor(territory: Territory){
        resources = Resources()
        this.territory = territory
        terId = territory.id
        location = Location(territory.x, territory.y)
        taxes = mutableMapOf(Territory.FLOUR_NAME to 0.1)
        laws = mutableSetOf()
    }

    constructor(other: Capital){
        resources = Resources(other.resources)
        territory = null //this will be overwritten in finishConstruction
        terId = other.territory!!.id
        location = Location(other.location)
        taxes = other.taxes.toMutableMap()
        laws = other.laws //so far laws are immutable
    }

    constructor(saveString: Map<String, Any>){
        resources = Resources(saveString["res"] as Map<String, Any>)
        territory = null //this will be overwritten in finishConstruction
        terId = saveString["ter"] as Int
        location = Location(saveString["loc"] as Map<String, Any>)
        taxes = (saveString["tax"] as Map<String, Double>).toMutableMap()
        laws = (saveString["laws"] as List<Map<String, Any>>).map { GlobalLawFactory.lawFromSaveString(it) }.toMutableSet()
    }

    fun finishConstruction(game: Game){
        territory = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terId)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "res" to resources.saveString(),
            "ter" to territory!!.id,
            "loc" to location.saveString(),
            "tax" to taxes,
            "laws" to laws.map { it.saveString() }
        )
    }

    fun generateCountTitle(): Title {
        return Count(this)
    }

    fun laws(): Set<Law>{
        return laws.toSet()
    }

    fun enactLaw(law: Law){
        laws.removeIf{it.type == law.type}
        laws.add(law)
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

    fun endTurn(){
        laws.forEach { it.apply(this) }
    }
}