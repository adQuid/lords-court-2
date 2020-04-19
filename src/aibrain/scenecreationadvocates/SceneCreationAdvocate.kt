package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.scenemaker.SceneMaker

abstract class SceneCreationAdvocate {

    abstract val me: ShortStateCharacter
    abstract val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter)

    fun weight(game: ShortStateGame): Double{
        val prospectiveScene = createScene(game, me).makeScene(game)
        if(prospectiveScene != null){
            return reactionAdvocates.map { adv -> adv.weight(game, prospectiveScene) }.sortedByDescending { it }[0]
        }
        return 0.0
    }

    abstract fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker

}