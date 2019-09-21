package main

import aibrain.BrainThread
import game.Game

var singleton: Controller? = null

class Controller {

    var game: Game? = null

    private val brainThread1 = Thread(BrainThread(this))

    constructor(){
        game = Game()
        brainThread1.start()
    }
}

fun main() {
    singleton = Controller()
}