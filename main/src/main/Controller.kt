package main

import aibrain.Brain
import aibrain.BrainThread
import game.Game

class Controller {

    var game: Game? = null

    val brainThread1 = Thread(BrainThread(this))

}

fun main() {
    var game = Game()
    var testBrain = Brain(game.players[1])
    println(testBrain.casesOfConcern(game))
}