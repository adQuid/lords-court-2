package main

import aibrain.BrainThread
import dialog.Line
import game.Conversation
import game.Game
import game.Player
import javafx.application.Application
import ui.MainUI



class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null

    private val brainThread1 = Thread(BrainThread(this))

    var activeConversations = mutableListOf<Conversation>()

    constructor(){
        singleton = this
        game = Game()
        brainThread1.start()

        Application.launch(MainUI::class.java)
    }

    //returns true if the conversation was created, false otherwise
    fun createConversation(initiator: Player, target: Player): Conversation?{
        activeConversations.forEach{conversation ->
            if(conversation.participants().intersect(listOf(initiator, target)).isNotEmpty()){
                return null
            }
        }
        val retval = Conversation(initiator, target)
        activeConversations.add(retval)
        return retval
    }

    fun endConversation(conversation: Conversation){
        activeConversations.remove(conversation)
    }
}

fun main() {
    Controller.singleton = Controller()
}