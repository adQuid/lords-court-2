package actions

import game.Action
import game.Game
import game.Player

class BakeCookies: Action.ActionType() {
    override fun doAction(game: Game, player: Player) {
        game.deliciousness = game.deliciousness++
    }

}