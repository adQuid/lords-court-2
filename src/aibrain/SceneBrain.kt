package aibrain

import aibrain.scenecreationadvocates.GoToBedroomAdvocate
import aibrain.scenecreationadvocates.GoToWorkroomAdvocate
import aibrain.scenecreationadvocates.SceneCreationAdvocate
import aibrain.scenecreationadvocates.TalkToImportantPersonAdvocate
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
        reactionAdvocates = listOf(
            TalkMoreAdvocate(longBrain.player),
            GoToBedAdvocate(longBrain.player),
            LeaveSceneAdvocate(longBrain.player),
            GetReportAdvocate(longBrain.player))
        creationAdvocates = listOf(
            GoToBedroomAdvocate(character),
            GoToWorkroomAdvocate(character),
            TalkToImportantPersonAdvocate(character))
    }

    fun reactToScene(shortGameScene: ShortGameScene, game: ShortStateController){
        reactionAdvocates.sortedByDescending { adv -> adv.weight(game, shortGameScene) }[0].doToScene(game, shortGameScene)
    }

    fun nextSceneIWantToBeIn(player: ShortStateCharacter, game: ShortStateGame): SceneMaker?{
        return creationAdvocates.sortedByDescending { adv -> adv.weight(game) }[0].createScene(game, player)
    }
}