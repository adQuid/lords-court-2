package aibrain

import aibrain.scenecreationadvocates.GoToBedroomAdvocate
import aibrain.scenecreationadvocates.SceneCreationAdvocate
import aibrain.scenecreationadvocates.TalkToImportantPersonAdvocate
import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.LeaveSceneAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import aibrain.scenereactionadvocates.TalkMoreAdvocate
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    private val reactionAdvocates: List<SceneReactionAdvocate>
    private val creationAdvocates: List<SceneCreationAdvocate>

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
        reactionAdvocates = listOf(TalkMoreAdvocate(longBrain.player), GoToBedAdvocate(longBrain.player), LeaveSceneAdvocate(longBrain.player))
        creationAdvocates = listOf(GoToBedroomAdvocate(longBrain.player), TalkToImportantPersonAdvocate(longBrain.player))
    }

    fun reactToScene(scene: Scene, game: ShortStateGame){
        reactionAdvocates.sortedByDescending { adv -> adv.weight(scene) }[0].doToScene(game, scene)
    }

    fun nextSceneIWantToBeIn(player: ShortStateCharacter, game: ShortStateGame): SceneMaker?{
        return creationAdvocates.sortedByDescending { adv -> adv.weight(game) }[0].createScene(game, player)
    }
}