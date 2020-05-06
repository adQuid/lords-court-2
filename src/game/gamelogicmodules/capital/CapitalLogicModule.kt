package game.gamelogicmodules.capital

import game.Effect
import game.Game
import game.GameCharacter
import game.GameLogicModule
import game.gamelogicmodules.territory.ActiveCropsReportFactory
import game.gamelogicmodules.territory.FoodStocksReportFactory
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import shortstate.report.ReportFactory
import kotlin.math.roundToInt

class CapitalLogicModule: GameLogicModule {

    companion object{
        val type = "capital"
        val CAPITAL_NAME = "cap"

        private fun capitalsFromSaveString(saveString: Map<String, Any>): Collection<Capital>{
            return (saveString[CAPITAL_NAME] as List<Map<String, Any>>).map{ map -> Capital(map)}
        }

        private fun reportFactories(territories: Collection<Capital>): List<ReportFactory>{
            return territories.map{ CapitalStocksReportFactory(it) }
        }
    }
    val capitals: Collection<Capital>

    constructor(capitals: Collection<Capital>): super(reportFactories(capitals), listOf(TerritoryLogicModule.type)){
        this.capitals = capitals
    }

    constructor(other: CapitalLogicModule, game: Game): super(reportFactories(other.capitals), listOf(TerritoryLogicModule.type)){
        this.capitals = other.capitals.map { Capital(it) }
    }

    constructor(saveString: Map<String, Any>, game: Game): super(reportFactories(capitalsFromSaveString(saveString)), listOf(TerritoryLogicModule.type)){
        this.capitals = (saveString[CAPITAL_NAME] as List<Map<String, Any>>).map { Capital(it) }
    }

    override fun finishConstruction(game: Game) {
        capitals.forEach { it.finishConstruction(game) }
    }

    override val type: String
        get() = CapitalLogicModule.type

    override fun endTurn(game: Game): List<Effect> {

        val territories = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories

        territories.forEach { ter ->

            val crops = ter.lastHarvest
            val capital = capitalOf(ter)
            val taxRate = capital.taxes.getOrDefault(Territory.FLOUR_NAME, 0.0)

            val totalExpectedHarvest = crops.sumBy { crop -> crop.quantity * crop.yield() }

            if(taxRate * totalExpectedHarvest <= ter.resources.get(Territory.FLOUR_NAME)){
                capital.resources.add(Territory.FLOUR_NAME, (taxRate * totalExpectedHarvest).roundToInt())
                ter.resources.add(Territory.FLOUR_NAME, -(taxRate * totalExpectedHarvest).roundToInt())
                println("paid ${taxRate * totalExpectedHarvest} in taxes")
            } else {
                println("They didn't have the taxes. What should we do here...")
            }

        }

        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            CAPITAL_NAME to capitals.map { it.saveString() }
        )
    }

    override fun value(perspective: GameCharacter): Double {
        return 0.0
    }

    fun capitalOf(territory: Territory): Capital{
        try{
            return capitals.filter { it.territory == territory }.first()
        }catch(ex: Exception){
            throw Exception("No capital for ${territory.name}")
        }
    }

    fun matchingCapital(capital: Capital): Capital {
        return capitals.filter { it == capital }.first()
    }

    fun capitalById(id: Int): Capital {
        return capitals.filter { it.terId == id }.first()
    }
}