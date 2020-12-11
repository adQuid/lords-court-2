package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.LeaveSceneAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.SceneMaker

abstract class SceneCreationAdvocate {

    abstract val me: ShortStateCharacter

    fun weight(game: ShortStateGame): SceneCreationWeight{
        val sceneMaker = createScene(game, me)
        val prospectiveScene = sceneMaker.makeScene(game)
        if(prospectiveScene != null){
            return reactionAdvocates(game).filter{it.doToScene(game, prospectiveScene).cost() + sceneMaker.cost() <= me.energy}.map { adv -> if(adv is LeaveSceneAdvocate){SceneCreationWeight(0.0,adv)} else { SceneCreationWeight(adv.weight(game, prospectiveScene),adv) }}
                .sortedByDescending { it.weight }.getOrElse(0, { _ -> SceneCreationWeight(0.0, GoToBedAdvocate(me.player))})
        }
        return SceneCreationWeight(0.0, LeaveSceneAdvocate(me.player))
    }

    abstract fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker

    abstract fun reactionAdvocates(game: ShortStateGame): List<SceneReactionAdvocate>

}