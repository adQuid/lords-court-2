package test.controller

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.TitleFactory
import javafx.scene.control.Button
import main.Controller
import org.junit.Test
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

class TestMultithreading {

    @Test
    fun testControllerRuns(){
        val testGame = testGame()
        var waitForSetup = true
        Thread{
            while(waitForSetup){
                //busy wait until the controller has started
                Thread.sleep(20)
            }
            Controller.singleton!!.concludeTurnForPlayer(testGame.players.first().player)
        }.start()

        TestWithController {
            Controller.singleton!!.game = testGame.game
            waitForSetup = false
            Controller.singleton!!.startPlaying()
            assert(true)
        }.doit()
    }

    @Test
    fun testDoubleEndingTurn(){
        val testGame = testGame()
        var waitForSetup = true
        Thread{
            while(waitForSetup){
                //busy wait until the controller has started
                Thread.sleep(20)
            }
            Controller.singleton!!.concludeTurnForPlayer(testGame.players.first().player)
            Controller.singleton!!.playerThread()
            Controller.singleton!!.concludeTurnForPlayer(testGame.players.first().player)
            Controller.singleton!!.playerThread()
        }.start()

        TestWithController {
            Controller.singleton!!.game = testGame.game
            waitForSetup = false
            Controller.singleton!!.startPlaying()
            assert(true)
        }.doit()
    }

    private fun testGame(): ShortStateGame{
        val logic = SlowModule()
        val game = Game(listOf(logic))

        game.addPlayer(GameCharacter("player "+game.nextID, "this should never come up in a test", false, logic.locations().first(), game))

        return ShortStateGame(game, game.locations().first())
    }

}

class SlowModuleTitles: TitleFactory {
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }
}

class SlowModule: GameLogicModule {

    override val type: String
        get() = "slow (TESTING ONLY)"

    val location = Location(0,0)

    constructor(): super(listOf(), SlowModuleTitles(), listOf())

    override fun finishConstruction(game: Game) {
        //do nothing
    }

    override fun endTurn(game: Game){
        //do nothing
    }

    override fun locations(): Collection<Location> {
        return listOf(location)
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf() //We won't save anything in this test
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun score(perspective: GameCharacter): Score {
        return Score()
    }

    override fun bottomButtons(perspective: ShortStateCharacter): List<Button> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}