package main

import aibrain.BrainThread
import game.Game
import javafx.application.Application
import shortstate.Room
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

}

fun main() {
    Controller.singleton = Controller()
}