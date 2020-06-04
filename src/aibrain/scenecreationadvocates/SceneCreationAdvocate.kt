package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.LeaveSceneAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.SceneMaker

abstract class SceneCreationAdvocate {

    abstract val me: ShortStateCharacter
    abstract val reactionAdvocates: List<SceneReactionAdvocate>

    fun weight(game: ShortStateGame): SceneCreationWeight{
        val prospectiveScene = createScene(game, me).makeScene(game)
        if(prospectiveScene != null){
            return reactionAdvocates.map { adv -> if(adv is LeaveSceneAdvocate){SceneCreationWeight(0.0,adv)} else { SceneCreationWeight(adv.weight(game, prospectiveScene),adv) }}
                .sortedByDescending { it.weight }[0]
        }
        return SceneCreationWeight(0.0, LeaveSceneAdvocate(me.player))
    }

    abstract fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker

}