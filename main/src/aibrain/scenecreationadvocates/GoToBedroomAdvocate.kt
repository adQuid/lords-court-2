package aibrain.scenecreationadvocates

import game.Character
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToBedroomAdvocate: SceneCreationAdvocate {

    val me: Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        return 1.0
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}