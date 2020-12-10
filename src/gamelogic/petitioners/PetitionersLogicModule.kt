package gamelogic.petitioners

import aibrain.Plan
import aibrain.Score
import game.Game
import game.GameCharacter
import game.GameLogicModule
import game.Location
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule

class PetitionersLogicModule: GameLogicModule {

    companion object{
        val type = "petitioners"
    }
    override val type = PetitionersLogicModule.type

    var petitionersByCapital: MutableMap<Int, Set<GameCharacter>>

    //A standby location to store minor petitioners who need not be simulated
    val bench: Location

    constructor(): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        petitionersByCapital = mutableMapOf()
        bench = Location(-1,-1)
    }

    constructor(other: PetitionersLogicModule, game: Game): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        petitionersByCapital = other.petitionersByCapital.entries
            .map { it.key to it.value.map { game.characterById(it.id) }.toSet() }.toMap().toMutableMap()

        bench = Location(other.bench)
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        bench = Location(saveString["bench"] as Map<String, Any>)
        //TODO: make this less bad
        petitionersByCapital = (saveString["petitioners"] as Map<String, List<Any>>)
            .map{ entry -> entry.key.toInt() to entry.value.map { game.characterById(it as Int) }.toSet()}.toMap().toMutableMap()
    }

    override fun finishConstruction(game: Game) {

    }

    override fun endTurn(game: Game) {

    }

    override fun locations(): Collection<Location> {
        //intentionally hides the bench, since it's not a location where anyone should be doing anything
        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        val petitionersMap = mutableMapOf<String, List<Any>>()
        petitionersByCapital.forEach { capital, petitioners -> petitionersMap[capital.toString()] = petitioners.map { it.id } }

        return mapOf(
            "bench" to bench.saveString(),
            "petitioners" to petitionersMap
        )
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        TODO("Not yet implemented")
    }

    override fun score(perspective: GameCharacter): Score {
        TODO("Not yet implemented")
    }

}