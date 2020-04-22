package test.ai

import aibrain.ForecastBrain
import org.junit.Test
import test.fixtures.*

class TestAI {

    @Test
    fun testForcastBrainBasic(){
        val game = soloTestGame()
        val character = game.players[0]
        val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...

        brain.thinkAboutNextTurn(game)

        //basic sanity checks
        assert(brain.player == character)
        assert(brain.lastCasesOfConcern!!.isNotEmpty())
        assert(brain.lastFavoriteEffects != null)
        assert(brain.lastActionsToCommitTo != null)
    }

    @Test
    fun testAILikesGoodActions(){
        val game = twoPlayerTestGame()

        val character = game.players[0]
        val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...

        brain.thinkAboutNextTurn(game)
        assert(brain.dealValueToMe(goodDeal(game.players)) > 0)
    }

    @Test
    fun testAIDislikesBadActions(){
        val game = twoPlayerTestGame()

        val character = game.players[0]
        val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...

        brain.thinkAboutNextTurn(game)
        assert(brain.dealValueToMe(badDeal(game.players)) < 0)
    }

    @Test
    fun testAIJudgesBasedOnAlreadyComittedActions(){
        val game = soloTestGame()

        val character = game.players[0]
        val brain = character.brain
        brain.thinkAboutNextTurn(game)
        assert(brain.dealValueToMe(oneTimeGoodDeal(game.players)) > 0)

        game.appendActionsForPlayer(character, listOf(DummyOneTimeGoodThing()))

        brain.thinkAboutNextTurn(game)
        assert(brain.dealValueToMe(oneTimeGoodDeal(game.players)) == 0.0)
    }
}