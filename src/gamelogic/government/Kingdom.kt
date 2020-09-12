package gamelogic.government

import game.Game
import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule

class Kingdom {

    val name: String
    val terIds: Set<Int>
    var territories: Set<Territory>?
    val taxes: MutableMap<String, Double>

    constructor(name: String, territories: Collection<Territory>){
        this.name = name
        this.territories = territories.toSet()
        terIds = territories.map{it.id}.toSet()
        taxes = mutableMapOf(ResourceTypes.FLOUR_NAME to 0.1)
    }

    constructor(other: Kingdom){
        this.name = other.name
        this.territories = null //this will be overwritten in finishConstruction
        terIds = other.terIds
        taxes = other.taxes.toMutableMap()
    }

    constructor(saveString: Map<String, Any>){
        name = saveString["nme"] as String
        this.territories = null //this will be overwritten in finishConstruction
        terIds = (saveString["ter"] as List<Int>).toSet()
        taxes = (saveString["tax"] as Map<String, Double>).toMutableMap()
    }

    fun finishConstruction(game: Game){
        territories = terIds.map{(game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(it)}.toSet()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "nme" to name,
            "ter" to territories!!.map{it.id},
            "tax" to taxes
        )
    }

    fun generateKingTitle(): King {
        return King(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kingdom

        if (terIds != other.terIds) return false

        return true
    }

    override fun hashCode(): Int {
        return terIds.sum()
    }
}