package aibrain

import game.Game
import scenario.tutorial.MayronScoreGen

interface SpecialScoreGenerator {

    companion object{
        val scoreMap = mapOf<String, (saveString: Map<String, Any>, game: Game) -> SpecialScoreGenerator>(
            MayronScoreGen.typeName to { saveString, game -> MayronScoreGen() }
        )
    }

    abstract fun score(game: Game): Score

    abstract fun saveString(): Map<String, Any>
}