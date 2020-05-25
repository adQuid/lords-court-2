package shortstate.room.action

import aibrain.Deal
import aibrain.UnfinishedDeal
import game.GameCharacter
import game.Writ
import game.action.ActionMemory
import main.UIGlobals
import shortstate.GameRules
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.RoomAction
import ui.specialdisplayables.contructorobjects.WritConstructor

class DraftWrit: RoomAction {

    val deal: Deal
    val name: String

    constructor(deal: Deal, name: String){
        this.deal = deal
        this.name = name
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.focusOn(WritConstructor(UnfinishedDeal(listOf(player.player))))
    }

    override fun cost(): Int {
        return GameRules.COST_TO_MAKE_WRIT
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        addWritToCharacter(player)
    }

    fun generateWrit(firstSigner: GameCharacter): Writ {
        return Writ(name, deal.toFinishedDeal(), listOf(firstSigner))
    }

    fun addWritToCharacter(character: ShortStateCharacter){
        val writToAdd = generateWrit(character.player)
        character.player.writs.add(generateWrit(character.player))
        character.player.memory.comittedActions.addAll(writToAdd.deal.actions.values.flatten().map { action -> ActionMemory(action) })
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Draft new Writ"
    }
}