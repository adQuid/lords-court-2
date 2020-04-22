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

    private val reactionAdvocates: List<SceneReactionAdvocate>
    private val creationAdvocates: List<SceneCreationAdvocate>

    constructor(character: ShortStateCharacter, longBrain: ForecastBrain){
        this.longBrain = longBrain
        creationAdvocates = listOf(
            GoToBedroomAdvocate(character),
            GoToWorkroomAdvocate(character),
            GoToThroneroomAdvocate(character),
            TalkToImportantPersonAdvocate(character))
        reactionAdvocates = creationAdvocates.flatMap { creationAdvocate -> creationAdvocate.reactionAdvocates } + LeaveSceneAdvocate(longBrain.player)
    }

    fun reactToScene(shortGameScene: ShortGameScene, game: ShortStateGame){
        //println(reactionAdvocates.sortedByDescending { adv -> adv.weight(game, shortGameScene) }.map{adv -> "${adv.javaClass}: ${adv.weight(game, shortGameScene)}"})
        reactionAdvocates.sortedByDescending { adv -> adv.weight(game, shortGameScene) }[0].doToScene(game, shortGameScene)
    }

    fun nextSceneIWantToBeIn(player: ShortStateCharacter, game: ShortStateGame): SceneMaker?{
        //println(creationAdvocates.sortedByDescending { adv -> adv.weight(game) }.map { adv -> "${adv.javaClass}: ${adv.weight(game)}" })
        return creationAdvocates.sortedByDescending { adv -> adv.weight(game) }[0].createScene(game, player)
    }
}