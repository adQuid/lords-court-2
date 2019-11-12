package test

import test.game.testGameCreation
import test.game.testGameEndTurn

fun main() {
    runAsTest("Game: Create game", { -> testGameCreation()})
    runAsTest("Game: End turn", { -> testGameEndTurn()})
    println("Tests complete!")
}

private fun runAsTest(name: String, function: () -> Unit){
    try {
        function()
        println("$name: PASSED")
    } catch (e: AssertionError){
        System.err.println("$name: FAILED")
    }
}
