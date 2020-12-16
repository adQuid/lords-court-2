package gamelogic.petitioners

import aibrain.Plan
import aibrain.Score
import game.Game
import game.GameCharacter
import game.GameLogicModule
import game.Location
import gamelogic.government.GovernmentLogicModule
import shortstate.dialog.linetypes.SimpleLine
import shortstate.linetriggers.LineTrigger
import shortstate.linetriggers.replyWithSimpleLine

class PetitionersLogicModule: GameLogicModule {

    companion object{
        val type = "petitioners"
    }
    override val type = PetitionersLogicModule.type

    //used only for construction
    var tempPet: Map<String, Any>? = null
    var petitionersByCapital: MutableMap<Int, MutableSet<GameCharacter>>

    //A standby location to store minor petitioners who need not be simulated
    val bench: Location

    constructor(): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        petitionersByCapital = mutableMapOf()
        bench = Location(-1,-1)
    }

    constructor(other: PetitionersLogicModule, game: Game): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        petitionersByCapital = other.petitionersByCapital.entries
            .map { it.key to it.value.map { game.characterById(it.id) }.toMutableSet() }.toMap().toMutableMap()

        bench = Location(other.bench)
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), PetitionersTitleFactory, listOf(GovernmentLogicModule.type)){
        bench = Location(saveString["bench"] as Map<String, Any>)
        petitionersByCapital = mutableMapOf()
        tempPet = saveString["petitioners"] as Map<String, Any>
    }

    override fun finishConstruction(game: Game) {
        if(tempPet != null){
            petitionersByCapital = (tempPet as Map<String, List<Any>>)
                .map{ entry -> entry.key.toInt() to entry.value.map { game.characterById(it as Int) }.toMutableSet()}.toMap().toMutableMap()
        }
    }

    override fun endTurn(game: Game) {
        val capitalLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        capitalLogic.capitals.forEach { capital ->
            if(!petitionersByCapital.containsKey(capital.terId)){
                petitionersByCapital[capital.terId] = mutableSetOf()
            }

            //move old petitioners away from the courts
            petitionersByCapital[capital.terId]!!.forEach { it.location = bench }

            //add new petitioners
            if(game.isLive && petitionersByCapital[capital.terId]!!.filter { it.location == capital.location }.isEmpty()){
                val toAdd = CommonPetitionersLibrary.charity(capital, game)
                petitionersByCapital[capital.terId]!!.add(toAdd)
                game.addPlayer(toAdd)
            }
        }
    }

    override fun locations(): Collection<Location> {
        return listOf(bench)
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
        return listOf()
    }

    override fun score(perspective: GameCharacter): Score {
        return Score()
    }

}