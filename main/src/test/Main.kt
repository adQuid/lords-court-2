package test

import test.ai.testForcastBrainBasic
import test.game.testGameCreation
import test.game.testGameEndTurn
import test.shortstate.testBasicConvo

var failures = 0

fun main() {
    runAsTest("Game: Create game", { -> testGameCreation()})
    runAsTest("Game: End turn", { -> testGameEndTurn()})
    runAsTest("AI: Basic", { -> testForcastBrainBasic()})
    runAsTest("Conversation: Basic", { -> testBasicConvo()})
    println("Tests complete! $failures failures")
}

private fun runAsTest(name: String, function: () -> Unit){
    try {
        function()
        println("$name: PASSED")
    } catch (e: AssertionError){
        System.err.println("$name: FAILED")
        failures++
    }
}
