package test.tutorial

import gamelogic.government.Count
import main.Controller
import org.junit.Test
import scenario.tutorial.TutorialGameSetup
import shortstate.dialog.linetypes.AbandonConversation
import shortstate.dialog.linetypes.Farewell
import shortstate.room.Room
import shortstate.room.action.GoToBed
import shortstate.scenemaker.ConversationMaker
import test.controller.TestWithController

class TestTutorialHappyPath {

    @Test
    fun testTutorialCompletion(){
        TestWithController {
            val testGame = TutorialGameSetup.setupAgricultureGame()
            Controller.singleton!!.newGame(testGame)

            val playerShortController = Controller.singleton!!.shortThreadForPlayer(testGame.playerCharacter())
            val shortGame = playerShortController.shortGame
            val shortPlayer = shortGame.shortPlayerForLongPlayer(testGame.playerCharacter())

            val king = testGame.players.filter { it.name == "Mayren" }.first()
            shortPlayer!!.nextSceneIWannaBeIn = ConversationMaker(shortPlayer, playerShortController.shortGame.shortPlayerForLongPlayer(king)!!, playerShortController.shortGame.location.roomByType(Room.RoomType.ETC))
            playerShortController.shortGame.shortGameScene!!.terminated = true

            while(playerShortController.shortGame.shortGameScene == null || playerShortController.shortGame.shortGameScene!!.characters.size < 2){
                Thread.sleep(20)
            }
            assert(playerShortController.shortGame.shortGameScene!!.hasAPC())

            val convoWithDad = playerShortController.shortGame.shortGameScene!!.conversation!!

            while(convoWithDad.lineOptions(shortPlayer).filter { !(it is Farewell) }.isNotEmpty()){
                //assumes that signing the writ will be earlier on the list than not signing the writ
                convoWithDad.submitLine(convoWithDad.lineOptions(shortPlayer).filter { !(it is AbandonConversation || it is Farewell) }.first(), shortGame)
                while(convoWithDad.lastSpeaker == shortPlayer){
                    Thread.sleep(20)
                }
            }

            assert(king.writs.filter { it.complete() }.isNotEmpty())

            GoToBed().doActionIfCanAfford(shortGame, shortPlayer)

            while(testGame.turn < 3){
                Thread.sleep(20)
            }

            assert(testGame.playerCharacter().titles.filter{it is Count }.isNotEmpty())

        }.doit()
    }


}