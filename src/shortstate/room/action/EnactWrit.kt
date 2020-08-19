package shortstate.room.action

import game.Writ
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.RoomAction

class EnactWrit: RoomAction {

    val writ: Writ

    constructor(writ: Writ){
        this.writ = writ
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return 5
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        if(!writ.complete()){
            throw Exception("Trying to enact an imcomplete writ!")
        }
        if(writ!!.deal.actions.filter{entry -> entry.value.filter { !it.isLegal(game.game, entry.key) }.isNotEmpty()}.isNotEmpty()){
            throw Exception("Trying to enact a writ that can't be completed!")
        }
        writ.deal.actions.keys.forEach {
            UIGlobals.appendActionsForPlayer(it, writ.deal.actions[it]!!.toList())
        }
        player.player.writs.remove(writ)
        player.player.brain.dealsILike =  player.player.brain.dealsILike!!.filter {
            it.key != writ.deal
        }
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Enact: (${writ})"
    }
}