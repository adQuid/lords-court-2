package shortstate.room.action

import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction
import shortstate.scenemaker.GoToRoomSoloMaker

class Wait: RoomAction() {
    override fun doAction(shortGameController: ShortStateController, player: ShortStateCharacter) {
        println("$player waits")
        player.energy -= 50
        player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player, shortGameController.shortGame.shortGameScene!!.room)
        shortGameController.endScene(shortGameController.shortGame.shortGameScene!!)//kinda defeats the point, huh?
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Wait here a while"
    }
}