package aibrain.scenereactionadvocates

import shortstate.Scene
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.room.action.GoToBed

class GoToBedAdvocate: SceneReactionAdvocate {

    val me: game.Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(scene: Scene): Double {
        if(scene.room.type == Room.RoomType.BEDROOM && scene.characters.size == 1){
            return 200.0 - scene.characters[0].energy //TODO: should I really be inferring my own identity like this?
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, scene: Scene) {
        GoToBed().doAction(game, scene!!.characters[0])
    }
}