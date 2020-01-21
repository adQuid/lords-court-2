package shortstate

import javafx.application.Platform
import main.Controller
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
        Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        var nextPlayer = shortGame.nextActingPlayer()
        while(nextPlayer != null){
            if(shortGame.scene == null){
                if(nextPlayer.player.npc && nextPlayer.nextSceneIWannaBeIn == null){
                    nextPlayer.decideNextScene(shortGame)
                }
                addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)//because of the starting scene, this should NEVER be null
            } else if(shortGame.scene!!.nextPlayerToDoSomething().player.npc){
                doAIIfAppropriate()
                if(shortGame.scene != null && shortGame.scene!!.hasAPC()){
                    Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
                }
            }
            Thread.sleep(200)
            nextPlayer = shortGame.nextActingPlayer()
        }
        println("finished run")
    }

    private fun doAIIfAppropriate(){
        shortGame.scene!!.nextPlayerToDoSomething().sceneBrain.reactToScene(shortGame.scene!!, this)
    }

    private fun addScene(player: ShortStateCharacter, creator: SceneMaker?){
        println("scene($creator) added for $player, who has ${player.energy} energy left")
        if(creator == null){
            println("OHH NO we need a new scene!!!!")
        }
        if(shortGame.scene == null){
            val toAdd = creator!!.makeScene(shortGame)
            if(toAdd != null){
                shortGame.scene = toAdd
            } else {
                println("OHH NO we need a new scene!!!!")
            }
        }
        player.nextSceneIWannaBeIn = null
        try{
            Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        } catch(exception: Exception){
            println("exception caught on addScene UI update")
            //Do nothing. This is scotch tape because sort state games might be made before UI starts
        }
    }

    fun endScene(scene: Scene){
        if(shortGame.scene == scene){
            println("scene ended")
            scene!!.characters.forEach { player -> player.energy -= 1}
            //if there was a conversation, characters might have learned something in this time
            if(scene!!.conversation != null){
                scene!!.characters.filter { character -> character.player.npc }.forEach { character -> character.player.brain.thinkAboutNextTurn(shortGame.game) }
            }
            shortGame.scene = null
        }
        if(scene.characters.filter { char -> !char.player.npc }.isNotEmpty()){
            Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        }
    }
}