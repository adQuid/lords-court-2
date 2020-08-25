package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import aibrain.scenereactionadvocates.WaitAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToBedroomAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter
    private val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super() {
        me = character
        reactionAdvocates = listOf(GoToBedAdvocate(me.player), WaitAdvocate(me.player))
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }

    override fun reactionAdvocates(game: ShortStateGame): List<SceneReactionAdvocate> {
        return reactionAdvocates
    }
}