package shortstate.room.action

import aibrain.Deal
import aibrain.UnfinishedDeal
import game.GameCharacter
import game.Writ
import game.action.ActionMemory
import main.UIGlobals
import shortstate.Conversation
import shortstate.GameRules
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.Line
import shortstate.room.RoomAction
import ui.specialdisplayables.contructorobjects.WritConstructor

class SayLine: RoomAction {

    val line: Line

    constructor(line: Line){
        this.line = line
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.focusOn(WritConstructor(UnfinishedDeal(listOf(player.player))))
    }

    override fun cost(): Int {
        return GameRules.COST_TO_MAKE_WRIT
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        val convo = game.shortGameScene!!.conversation!!
        convo.submitLine(line, game)
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Say ${line.tooltipName()}"
    }
}