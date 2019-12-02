package aibrain.scenecreationadvocates

import game.Character
import shortstate.ShortStateGame
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToBedroomAdvocate: SceneCreationAdvocate {

    private val me: Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        return 950.0 - game.shortPlayerForLongPlayer(me)!!.energy
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}