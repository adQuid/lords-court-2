package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToBedroomAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter
    override val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super() {
        me = character
        reactionAdvocates = listOf(GoToBedAdvocate(me.player))
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}