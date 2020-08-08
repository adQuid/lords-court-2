package shortstate.room.action

import main.Controller
import shortstate.GameRules
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction
import shortstate.scenemaker.GoToRoomSoloMaker

class EndScene: RoomAction() {

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return 0
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        if(game.shortGameScene!!.conversation != null){
            throw Exception("Tried to end scene out of a conversation!")
        }
        game.shortGameScene!!.terminated = true
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Leave"
    }
}