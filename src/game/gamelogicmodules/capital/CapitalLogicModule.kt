package game.gamelogicmodules.capital

import game.Effect
import game.Game
import game.GameCharacter
import game.GameLogicModule

class CapitalLogicModule: GameLogicModule {

    companion object{
        val type = "capital"
        val CAPITAL_NAME = "cap"
    }
    val capitals: Collection<Capital>

    constructor(capitals: Collection<Capital>): super(listOf()){
        this.capitals = capitals
    }

    constructor(other: CapitalLogicModule, game: Game): super(listOf()){
        this.capitals = other.capitals.map { Capital(it) }
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf()){
        this.capitals = (saveString[CAPITAL_NAME] as List<Map<String, Any>>).map { Capital(it) }
    }

    override fun finishConstruction(game: Game) {
        capitals.forEach { it.finishConstruction(game) }
    }

    override val type: String
        get() = CapitalLogicModule.type

    override fun endTurn(): List<Effect> {
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
}