package gamelogic.playerresources

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.TitleFactory
import javafx.scene.control.Button
import shortstate.ShortStateCharacter

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
        //do nothing
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

        retval.add("gold", "I'll have money", perspective.resources.get(PlayerResourceTypes.GOLD_NAME).toDouble())
        retval.add("fish", "I'll have fish", perspective.resources.get(PlayerResourceTypes.FISH_NAME).toDouble() * 0.5)

        return retval
    }

    override fun bottomButtons(perspective: ShortStateCharacter): List<Button> {
        return listOf()
    }

}

class EmptyTitleFactory: TitleFactory{
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }

}