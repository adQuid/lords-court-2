package aibrain

import aibrain.scenecreationadvocates.*
import aibrain.scenereactionadvocates.*
import game.GameCharacter
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain
    val me: ShortStateCharacter

    private val creationAdvocates: List<SceneCreationAdvocate>

    constructor(character: ShortStateCharacter, longBrain: ForecastBrain){
        this.longBrain = longBrain
        this.me = character
        creationAdvocates = listOf(
            GoToBedroomAdvocate(character),
            GoToWorkroomAdvocate(character),
            GoToThroneroomAdvocate(character),
            TalkToImportantPersonAdvocate(character))
    }

    fun reactToScene(shortGameScene: ShortGameScene, game: ShortStateGame){
        val shortMe = game.shortPlayerForLongPlayer(longBrain.player)!!

        if(shortGameScene.conversation?.lastLine != null && !shortGameScene.conversation.lastLine!!.canChangeTopic()){
            return reactionAdvocates(game).filter { it is TalkMoreAdvocate }.first().doToScene(game, shortGameScene).doActionIfCanAfford(game, shortMe)
        }

        reactionAdvocates(game).filter{adv -> adv.weight(game, shortGameScene) > 0}.filter{shortMe.canAffordAction(it.doToScene(game, shortGameScene))}
            .sortedByDescending { adv -> adv.weight(game, shortGameScene) }.getOrElse(0, {LeaveSceneAdvocate(me.player)}).doToScene(game, shortGameScene).doActionIfCanAfford(game, shortMe)
    }

    fun nextSceneIWantToBeIn(player: ShortStateCharacter, game: ShortStateGame): SceneMaker?{
        val debug = creationAdvocates.sortedByDescending { adv -> adv.weight(game).weight }.map { adv -> adv.weight(game) }
        return bestCreationAdvocate(game).createScene(game, player)
    }

    fun bestCreationAdvocate(game: ShortStateGame): SceneCreationAdvocate {
        return creationAdvocates.sortedByDescending { adv -> adv.weight(game).weight }[0]
    }

    private fun reactionAdvocates(game: ShortStateGame): Collection<SceneReactionAdvocate>{
        return creationAdvocates.flatMap { creationAdvocate -> creationAdvocate.reactionAdvocates(game) } + LeaveSceneAdvocate(longBrain.player)
    }
}