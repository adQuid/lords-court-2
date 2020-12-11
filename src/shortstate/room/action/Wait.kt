package shortstate.room.action

import shortstate.GameRules
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.RoomAction
import shortstate.scenemaker.GoToRoomSoloMaker

class Wait: RoomAction() {

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return GameRules.COST_TO_WAIT
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("$player waits")
        if(game.sceneForPlayer(player)!!.conversation != null){
            throw Exception("Character tried to wait in conversation!")
        }
        game.pass(player)
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Wait here a while"
    }
}