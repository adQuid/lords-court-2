package aibrain.scenecreationadvocates

import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToBedroomAdvocate: SceneCreationAdvocate {

    private val me: ShortStateCharacter

    constructor(character: ShortStateCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        return (800.0 / (me.energy + 0.1))
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}