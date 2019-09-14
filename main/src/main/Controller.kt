package main

import aibrain.Brain
import game.Game

class Controller {



}

fun main() {
    var game = Game()
    var testBrain = Brain(game.players.get(1))
    testBrain.determineIdealPlan(game)
}