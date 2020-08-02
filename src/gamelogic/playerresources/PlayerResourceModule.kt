package gamelogic.playerresources

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.TitleFactory
import javafx.scene.control.Button
import shortstate.ShortStateCharacter

class PlayerResourceModule: GameLogicModule {

    override val type: String
        get() = "player resources"

    constructor(): super(listOf(), EmptyTitleFactory(), listOf())

    constructor(other: PlayerResourceModule): super(listOf(), EmptyTitleFactory(), listOf())

    override fun finishConstruction(game: Game) {

    }

    override fun endTurn(game: Game): List<Effect> {
        return listOf()
    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun effectFromSaveString(saveString: Map<String, Any>, game: Game): Effect? {
        return null
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

        retval.add("gold", "i'll have money", perspective.resources.get(PlayerResourceTypes.GOLD_NAME).toDouble())

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