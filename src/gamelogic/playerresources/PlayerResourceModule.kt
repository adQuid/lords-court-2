package gamelogic.playerresources

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.TitleFactory
import javafx.scene.control.Button
import shortstate.ShortStateCharacter
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

        val gold = perspective.resources.get(PlayerResourceTypes.GOLD_NAME).toDouble()
        retval.add("gold", {value -> "${perspective} will ${DialogFormatter.gainOrLose(value)} gold pieces"}, gold)
        retval.add("fish", {value -> "${perspective} will ${DialogFormatter.gainOrLose(value * 10)} fish"}, perspective.resources.get(PlayerResourceTypes.FISH_NAME).toDouble() * 0.1)

        return retval
    }

}

class EmptyTitleFactory: TitleFactory{
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }

}