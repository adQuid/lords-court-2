package gamelogic.government

import aibrain.Plan
import aibrain.Score
import game.*
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import javafx.scene.control.Button
import shortstate.ShortStateCharacter
import shortstate.report.ReportFactory
import shortstate.room.RoomActionMaker
import kotlin.math.roundToInt

class GovernmentLogicModule: GameLogicModule {

    companion object{
        val type = "capital"
        val CAPITAL_NAME = "cap"
        val KINGDOM_NAME = "kng"

        private fun capitalsFromSaveString(saveString: Map<String, Any>): Collection<Capital>{
            return (saveString[CAPITAL_NAME] as List<Map<String, Any>>).map{ map -> Capital(map)}
        }

        private fun reportFactories(territories: Collection<Capital>): List<ReportFactory>{
            return territories.map{ CapitalStocksReportFactory(it) }
        }

        fun capitalById(game: Game, id: Int): Capital{
            val module = game.moduleOfType(type) as GovernmentLogicModule

            return module.capitalById(id)
        }
    }
    val capitals: Collection<Capital>
    val kingdoms: Collection<Kingdom>

    constructor(capitals: Collection<Capital>, kingdoms: Collection<Kingdom>): super(reportFactories(capitals), CapitalTitleFactory, listOf(TerritoryLogicModule.type)){
        this.capitals = capitals
        this.kingdoms = kingdoms
    }

    constructor(other: GovernmentLogicModule, game: Game): super(reportFactories(other.capitals), CapitalTitleFactory, listOf(TerritoryLogicModule.type)){
        this.capitals = other.capitals.map { Capital(it) }
        this.kingdoms = other.kingdoms.map { Kingdom(it)}
    }

    constructor(saveString: Map<String, Any>, game: Game): super(reportFactories(capitalsFromSaveString(saveString)), CapitalTitleFactory, listOf(TerritoryLogicModule.type)){
        this.capitals = (saveString[CAPITAL_NAME] as List<Map<String, Any>>).map { Capital(it) }
        this.kingdoms = (saveString[KINGDOM_NAME] as List<Map<String, Any>>).map { Kingdom(it) }
    }

    override fun finishConstruction(game: Game) {
        capitals.forEach { it.finishConstruction(game) }
        kingdoms.forEach { it.finishConstruction(game) }
    }

    override val type: String
        get() = GovernmentLogicModule.type

    override fun locations(): Collection<Location> {
        return capitals.map { it.location }
    }

    override fun endTurn(game: Game){

        val territories = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map.territories

        territories.forEach { ter ->

            val crops = ter.lastHarvest
            val capital = capitalOf(ter)
            val taxRate = capital.taxes.getOrDefault(Territory.FLOUR_NAME, 0.0)

            val totalExpectedHarvest = crops.sumBy { crop -> crop.quantity * crop.yield() }

            if(taxRate * totalExpectedHarvest <= ter.resources.get(Territory.FLOUR_NAME)){
                capital.resources.add(Territory.FLOUR_NAME, (taxRate * totalExpectedHarvest).roundToInt())
                ter.resources.add(Territory.FLOUR_NAME, -(taxRate * totalExpectedHarvest).roundToInt())
            } else if (game.isLive) {
                println("They didn't have the taxes. What should we do here...")
            }

        }
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            CAPITAL_NAME to capitals.map { it.saveString() },
            KINGDOM_NAME to kingdoms.map { it.saveString() }
        )
    }

    override fun score(perspective: GameCharacter): Score {
        var retval = Score()

        capitals.forEach {
            if(countOfCaptial(it.terId) == perspective){
                val flourGained = it.resources.get(Territory.FLOUR_NAME)
                retval.add("Flour Gained", {value -> "we will add ${value} flour to our stockpile"}, flourGained.toDouble())

                val population = it.resources.get(Territory.POPULATION_NAME)
                retval.add("", {value -> "pop change"}, population.toDouble() * 2.0)

                val newCrops = it.territory!!.crops.sumBy { crop -> crop.quantity }
                retval.add("New Crops", {value -> "${value} will be planted"}, newCrops.toDouble())
            }
        }

        return retval
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        val retval = mutableListOf<Plan>()

        importantPlayers.forEach { player ->
            player.titles.forEach { title ->

                if(title is Count){
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.0)),0.2))
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.1)),0.2))
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.2)),0.2))
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.25)),0.2))
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.3)),0.2))
                    retval.add(Plan(player, listOf(SetTaxRate(title.capital.terId, 0.35)),0.2))
                }
            }
        }

        return retval
    }

    fun capitalOf(territory: Territory): Capital{
        try{
            return capitals.filter { it.territory == territory }.first()
        }catch(ex: Exception){
            throw Exception("No capital for ${territory.name}")
        }
    }

    fun kingdomOf(territory: Territory): Kingdom?{
        return kingdoms.firstOrNull { it.territories!!.contains(territory) }
    }

    fun matchingCapital(capital: Capital): Capital {
        return capitals.filter { it == capital }.first()
    }

    fun capitalById(id: Int): Capital {
        return capitals.filter { it.terId == id }.first()
    }

    fun kingdomByName(name: String): Kingdom {
        return kingdoms.filter{ it.name == name }.first()
    }

    fun countOfCaptial(terId: Int): GameCharacter?{
        val countTitle = parent!!.titles.filter { it is Count && it.capital.territory!!.id == terId }.firstOrNull()
        return parent!!.players.filter { it.titles.contains(countTitle) }.firstOrNull()
    }

    fun kingOfKingdom(kingdom: String): GameCharacter?{
        val kingTitle = parent!!.titles.filter { it is King && it.kingdom.name == kingdom }.firstOrNull()
        return parent!!.players.filter { it.titles.contains(kingTitle) }.firstOrNull()
    }

    fun legalActionsReguarding(player: GameCharacter, capital: Capital): List<RoomActionMaker>{
        return listOf()
    }
}