package test.ai

import aibrain.ForecastBrain
import test.util.soloTestGame

fun testForcastBrainBasic(){
    val game = soloTestGame()
    val character = game.players[0]
    val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...

    brain.thinkAboutNextTurn(game)

    //basic sanity checks
    assert(brain.player == character)
    assert(brain.sortedCases!!.isNotEmpty())
    assert(brain.lastFavoriteEffects != null)
    assert(brain.lastActionsToCommitTo != null)
}