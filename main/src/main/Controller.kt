package main

import game.action.Action
import aibrain.BrainThread
import game.Game
import game.Character
import javafx.application.Application
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
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

    fun sceneForPlayer(player: ShortStateCharacter): Scene?{
        return shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: Character, actions: List<Action>){
        game!!.commitActionsForPlayer(player, actions)
        if(game!!.actionsByPlayer.keys.size == game!!.players.size){
            println("ENDING TURN")
            game!!.endTurn()
            shortGame = ShortStateGame(game!!, game!!.locations[0])
            println("Deliciousness: ${game!!.deliciousness}")
        }
    }
}

fun main() {
    Controller.singleton = Controller()
}