package main

import action.Action
import aibrain.BrainThread
import game.Game
import game.Player
import javafx.application.Application
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import ui.MainUI

class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null
    var shortGame: ShortStateGame? = null
    var GUI: MainUI? = null

    private val brainThread1 = Thread(BrainThread(this))

    constructor(){
        singleton = this
        game = Game()
        shortGame = ShortStateGame(game!!, game!!.locations[0])
        brainThread1.start()

        Application.launch(MainUI::class.java)
    }

    fun sceneForPlayer(player: ShortStatePlayer): Scene?{
        return shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: Player, actions: List<Action>){
        game!!.commitActionsForPlayer(player, actions)
        if(game!!.actionsByPlayer.keys.size == game!!.players.size){
            println("ENDING TURN")
            game!!.endTurn()
            shortGame = ShortStateGame(game!!, game!!.locations[0])
            GUI!!.refocus()
        }
    }
}

fun main() {
    Controller.singleton = Controller()
}