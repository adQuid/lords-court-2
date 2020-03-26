package shortstate

import javafx.application.Platform
import main.Controller
import main.UIGlobals
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class ShortStateController: Runnable {

    val shortGame: ShortStateGame

    constructor(shortGame: ShortStateGame){
        this.shortGame = shortGame
        establishStartingScene()
    }

    private fun establishStartingScene(){
        shortGame.players.forEach { player ->
            player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player,shortGame.location.startRoom())
        }
        //this SHOULD be safe, since the game just started. this will crash on an empty location
        addScene(shortGame.nextActingPlayer()!!, shortGame.nextActingPlayer()!!.nextSceneIWannaBeIn)
    }

    override fun run(){
        while(Controller.singleton!!.GUI == null){
            Thread.sleep(100) //Very weak logic to prevent the threads from crashing the GUI
        }
        println("starting run")
        Platform.runLater { UIGlobals.GUI().resetFocus() }
        var nextPlayer = shortGame.nextActingPlayer()
        while(nextPlayer != null){
            if(shortGame.shortGameScene == null){
                if(nextPlayer.player.npc && nextPlayer.nextSceneIWannaBeIn == null){
                    nextPlayer.decideNextScene(shortGame)
                }
                addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)//because of the starting scene, this should NEVER be null
            } else if(shortGame.shortGameScene!!.nextPlayerToDoSomething().player.npc){
                doAIIfAppropriate()
                if(shortGame.shortGameScene != null && shortGame.shortGameScene!!.hasAPC()){
                    Platform.runLater { UIGlobals.GUI().resetFocus() }
                }
            }
            Thread.sleep(200)
            nextPlayer = shortGame.nextActingPlayer()
        }
        println("finished run")
    }

    private fun doAIIfAppropriate(){
        shortGame.shortGameScene!!.nextPlayerToDoSomething().sceneBrain.reactToScene(shortGame.shortGameScene!!, this)
    }

    private fun addScene(player: ShortStateCharacter, creator: SceneMaker?){
        println("scene($creator) added for $player, who has ${player.energy} energy left")
        if(creator == null){
            println("OHH NO we need a new scene!!!!")
        }
        if(shortGame.shortGameScene == null){
            val toAdd = creator!!.makeScene(shortGame)
            if(toAdd != null){
                shortGame.shortGameScene = toAdd
            } else {
                println("OHH NO we need a new scene!!!!")
            }
        }
        player.nextSceneIWannaBeIn = null
        try{
            Platform.runLater { UIGlobals.GUI().resetFocus() }
        } catch(exception: Exception){
            println("exception caught on addScene UI update")
            //Do nothing. This is scotch tape because sort state games might be made before UI starts
        }
    }

    fun endScene(shortGameScene: ShortGameScene){
        if(shortGame.shortGameScene == shortGameScene){
            println("scene ended")
            shortGameScene!!.characters.forEach { player -> player.energy -= 1}
            //if there was a conversation, characters might have learned something in this time
            if(shortGameScene!!.conversation != null){
                shortGameScene!!.characters.filter { character -> character.player.npc }.forEach { character -> character.player.brain.thinkAboutNextTurn(shortGame.game) }
            }
            shortGame.shortGameScene = null
        }
        if(shortGameScene.characters.filter { char -> !char.player.npc }.isNotEmpty()){
            Platform.runLater { UIGlobals.GUI().resetFocus() }
        }
    }
}