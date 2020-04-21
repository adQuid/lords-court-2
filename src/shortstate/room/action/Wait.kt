package shortstate.room.action

import main.Controller
import shortstate.GameRules
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction
import shortstate.scenemaker.GoToRoomSoloMaker

class Wait: RoomAction() {

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doAction(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("$player waits")
        player.energy -= GameRules.COST_TO_WAIT
        player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player, game.shortGameScene!!.room)
        game.shortGameScene!!.terminated = true//kinda defeats the point, huh?
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Wait here a while"
    }
}