package gamelogic.playerresources

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.TitleFactory
import gamelogic.government.GovernmentLogicModule
import gamelogic.resources.Resources
import gamelogic.territory.TerritoryLogicModule
import shortstate.dialog.DialogFormatter

class PlayerResourceModule: GameLogicModule {

    companion object{
        val type = "player resources"
    }

    override val type: String
        get() = Companion.type

    constructor(): super(listOf(), EmptyTitleFactory(), listOf())

    constructor(other: PlayerResourceModule): super(listOf(), EmptyTitleFactory(), listOf())

    constructor(saveString: Map<String, Any>, context: Game): super(listOf(), EmptyTitleFactory(), listOf())

    override fun finishConstruction(game: Game) {

    }

    override fun endTurn(game: Game) {
        //This module isn't dependent on the others, so it needs to be ready for that module not to be there
        val governmentLogic = game.gameLogicModules.filter { it is GovernmentLogicModule }.map{it as GovernmentLogicModule}.firstOrNull()
        val territoryLogic = game.gameLogicModules.filter { it is TerritoryLogicModule }.map{it as TerritoryLogicModule}.firstOrNull()

        if(governmentLogic != null && territoryLogic != null){
            game.players.forEach {
                val capitalWherePlayerIs = governmentLogic.capitalByLocation(it.location)
                if(it == governmentLogic.countOfCaptial(capitalWherePlayerIs.terId)){
                    it.privateResources.resources.map { capitalWherePlayerIs.resources.add(it.key, it.value) }
                    it.privateResources.resources.clear()
                }
            }
        }
    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        return listOf()
    }

    override fun score(perspective: GameCharacter): Score {
        val retval = Score()

        val gold = perspective.privateResources.get(PlayerResourceTypes.GOLD_NAME).toDouble()
        retval.add("gold", {value -> "${perspective} will ${DialogFormatter.gainOrLose(value)} gold pieces"}, gold)
        retval.add("fish", {value -> "${perspective} will ${DialogFormatter.gainOrLose(value * 10)} fish"}, perspective.privateResources.get(PlayerResourceTypes.FISH_NAME).toDouble() * 0.1)

        return retval
    }

    fun accessableResourcesForPlayer(game: Game, player: GameCharacter): Resources{
        //This module isn't dependent on the others, so it needs to be ready for that module not to be there
        val governmentLogic = game.gameLogicModules.filter { it is GovernmentLogicModule }.map{it as GovernmentLogicModule}.firstOrNull()
        val territoryLogic = game.gameLogicModules.filter { it is TerritoryLogicModule }.map{it as TerritoryLogicModule}.firstOrNull()

        val retval = Resources(player.privateResources)
        if(governmentLogic != null && territoryLogic != null){
            val capitalWherePlayerIs = governmentLogic.capitalByLocation(player.location)
            if( player == governmentLogic.countOfCaptial(capitalWherePlayerIs.terId)){
                retval.addAll(capitalWherePlayerIs.resources)
            }
        }
        return retval
    }

}

class EmptyTitleFactory: TitleFactory{
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }

}