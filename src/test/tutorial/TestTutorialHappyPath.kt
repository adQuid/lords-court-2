package test.tutorial

import main.Controller
import org.junit.Test
import scenario.tutorial.TutorialGameSetup
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import test.controller.TestWithController

class TestTutorialHappyPath {

    @Test
    fun testTutorialCompletion(){
        TestWithController {
            val testGame = TutorialGameSetup.setupAgricultureGame()
            Controller.singleton!!.newGame(testGame) //This will hang if the AI doesn't find a way to finish the round
            val playerShortController = Controller.singleton!!.shortThreadForPlayer(testGame.playerCharacter())
            val shortPlayer = playerShortController.shortGame.shortPlayerForLongPlayer(testGame.playerCharacter())
            val king = testGame.players.filter { it.name == "Mayren" }.first()
            shortPlayer!!.nextSceneIWannaBeIn = ConversationMaker(shortPlayer, playerShortController.shortGame.shortPlayerForLongPlayer(king)!!, playerShortController.shortGame.location.roomByType(Room.RoomType.ETC))
            playerShortController.shortGame.shortGameScene!!.terminated = true

            while(playerShortController.shortGame.shortGameScene == null || playerShortController.shortGame.shortGameScene!!.characters.size < 2){
                Thread.sleep(20)
            }
            assert(playerShortController.shortGame.shortGameScene!!.hasAPC())

        }.doit()
    }


}