package shortstate

import javafx.application.Platform
import main.Controller
import main.UIGlobals
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.specialdisplayables.Message
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.selectionmodal.SelectionModal

class ShortStateController: Runnable {

    val shortGame: ShortStateGame
    var started = false
    var finished = false

    constructor(shortGame: ShortStateGame){
        this.shortGame = shortGame
        if(shortGame.nextActingPlayer() != null){
            establishStartingScene()
        }
    }

    private fun establishStartingScene(){
        shortGame.players.forEach { player ->
            player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player,shortGame.location.startRoom())
        }
        //this SHOULD be safe, since the game just started. this will crash on an empty location
        addScene(shortGame.nextActingPlayer()!!, shortGame.nextActingPlayer()!!.nextSceneIWannaBeIn)
    }

    override fun run(){
        println("starting run")
        started = true
        var nextPlayer = shortGame.nextActingPlayer()
        while(nextPlayer != null){
            Thread.sleep(20)
            if(shortGame.shortGameScene == null){
                if(nextPlayer.nextSceneIWannaBeIn == null){
                    if(nextPlayer.player.npc){
                        nextPlayer.decideNextScene(shortGame)
                    } else {
                        if(UIGlobals.GUI().curFocus.isEmpty() || !(UIGlobals.GUI().curFocus.any{it is ShortGameScene})){ //super sketchy check to see if the player is already looking at a scene selector
                            if(shortGame.players.contains(UIGlobals.playingAs())){
                                UIGlobals.focusOn(NewSceneSelector.newSceneSelector(nextPlayer))
                            }
                        }
                        continue
                    }
                }
                addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)//because of the starting scene, this should NEVER be null
            } else {
                if(shortGame.shortGameScene!!.terminated){
                    endScene(shortGame.shortGameScene!!)
                }else if(shortGame.shortGameScene!!.nextPlayerToDoSomething().player.npc){
                    doAIIfAppropriate()
                    if(shortGame.shortGameScene != null && shortGame.shortGameScene!!.hasAPC()){
                        Platform.runLater { UIGlobals.resetFocus() }
                    }
                }
            }
            nextPlayer = shortGame.nextActingPlayer()
        }
        println("finished run")
        finished=true
        Controller.singleton!!.runAnotherThread()
    }

    private fun doAIIfAppropriate(){
        shortGame.shortGameScene!!.nextPlayerToDoSomething().sceneBrain.reactToScene(shortGame.shortGameScene!!, shortGame)
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
            shortGame.shortGameScene!!.characters.forEach { it.done = false; Controller.singleton!!.concludeTurnForPlayer(it.player, true) }
        }
        player.nextSceneIWannaBeIn = null
        try{
            if(!player.player.npc){
                Platform.runLater { UIGlobals.resetFocus() }
            }
        } catch(exception: Exception){
            //println("exception caught on addScene UI update: ${exception.toString()}")
            //Do nothing. This is scotch tape because sort state games might be made before UI starts
        }
    }

    fun endScene(shortGameScene: ShortGameScene){
        var partingMessage: Message? = null
        if(shortGame.shortGameScene == shortGameScene){
            shortGameScene.characters.forEach { player -> player.energy -= GameRules.COST_TO_END_SCENE}
            //if there was a conversation, characters might have learned something in this time
            if(shortGameScene.conversation != null){
                shortGameScene.characters.filter { character -> character.player.npc }.forEach { character -> character.player.brain.thinkAboutNextTurn(shortGame.game); character.decideNextScene(shortGame) }
                if(shortGameScene.characters.any { character -> !character.player.npc }){
                    partingMessage = Message(shortGameScene.conversation.lastSpeaker.player.name+" walks away")
                }
            }
            if(shortGameScene.characters.any { character -> !character.player.npc }){
                UIGlobals.defocus()
            }
            shortGame.shortGameScene = null

            if(partingMessage != null){
                UIGlobals.focusOn(partingMessage)
            }

        }
        if(shortGameScene.characters.filter { char -> !char.player.npc }.isNotEmpty()){
            Platform.runLater { UIGlobals.refresh() }
        }
    }
}