package main

import aibrain.BrainThread
import game.Conversation
import game.Game
import game.Player
import javafx.application.Application
import shortstate.ShortStateGame
import ui.MainUI

class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null
    var shortGame: ShortStateGame? = null

    private val brainThread1 = Thread(BrainThread(this))



    constructor(){
        singleton = this
        game = Game()
        shortGame = ShortStateGame(game!!)
        brainThread1.start()

        Application.launch(MainUI::class.java)
    }

    //returns true if the conversation was created, false otherwise
    fun createConversation(initiator: Player, target: Player): Conversation?{
        shortGame!!.activeConversations.forEach{conversation ->
            if(conversation.participants().intersect(listOf(initiator, target)).isNotEmpty()){
                return null
            }
        }
        val retval = Conversation(initiator, target)
        shortGame!!.activeConversations.add(retval)
        return retval
    }

    fun endConversation(conversation: Conversation){
        shortGame!!.activeConversations.remove(conversation)
    }
}

fun main() {
    Controller.singleton = Controller()
}