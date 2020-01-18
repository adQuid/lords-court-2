package shortstate

import game.Game
import game.Location
import game.Character
import javafx.application.Platform
import main.Controller
import shortstate.room.action.GoToBed
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class ShortStateGame: Runnable {

    val SCENE_NAME = "SCENE"
    private var scene: Scene? = null
    val game: Game
    val LOCATION_NAME = "LOCATION"
    val location: Location
    val PLAYERS_NAME = "PLAYERS"
    val players: List<ShortStateCharacter>

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStateCharacter(player) }
        establishStartingScene()
    }

    constructor(parent: Game, saveString: Map<String,Any>){
        game = parent
        location = parent.locationById(saveString[LOCATION_NAME] as Int)
        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> ShortStateCharacter(parent, map) }
        establishStartingScene()
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            SCENE_NAME to {scene!!.saveString(); if(scene != null) else null},
            LOCATION_NAME to location.id,
            PLAYERS_NAME to players.map { player -> player.saveString() }
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is ShortStateGame){
            return this.game == other.game &&
            this.location.id == other.location.id &&
            this.players == other.players
        } else {
            return false
        }
    }

    @Synchronized fun nextPlayerToDoShortStateStuff(): ShortStateCharacter?{
        players.sortedByDescending { player -> player.energy }.forEach {
            if(it.player.npc && it.energy > 0 && it.nextSceneIWannaBeIn == null){
                return it
            }
        }
        return null
    }

    //returns short state player actually being played by a human
    fun playerCharacter(): ShortStateCharacter{
        players.forEach {
            if(!it.player.npc){
                return it
            }
        }
        throw Exception("No player found!")
    }

    fun shortPlayerForLongPlayer(player: Character): ShortStateCharacter?{
        players.forEach {
            if(it.player == player){
                return it
            }
        }
        return null
    }

    fun nextActingPlayer(): ShortStateCharacter?{
        return players.filter { it.energy > 0 }.sortedByDescending { it.energy }.getOrNull(0)
    }

    fun sceneForPlayer(player: ShortStateCharacter): Scene?{
        if(scene != null && scene!!.characters!!.contains(player)){
            return scene
        }

        return null
    }

    private fun establishStartingScene(){
        players.forEach { player ->
            player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player,location.startRoom())
        }
        //this SHOULD be safe, since the game just started. this will crash on an empty location
        addScene(nextActingPlayer()!!, nextActingPlayer()!!.nextSceneIWannaBeIn)
    }

    override fun run(){
        while(Controller.singleton!!.GUI == null){
            Thread.sleep(100) //Very weak logic to prevent the threads from crashing the GUI
        }
        println("starting run")
        Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        var nextPlayer = nextActingPlayer()
        while(nextPlayer != null){
            if(scene == null){
                if(nextPlayer.player.npc && nextPlayer.nextSceneIWannaBeIn == null){
                    nextPlayer.decideNextScene(this)
                }
                addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)
            } else if(scene!!.nextPlayerToDoSomething().player.npc){
                doAIIfAppropriate()
                if(scene != null && scene!!.hasAPC()){
                    Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
                }
            }
            Thread.sleep(200)
            nextPlayer = nextActingPlayer()
        }
        println("finished run")
    }

    private fun doAIIfAppropriate(){
        scene!!.nextPlayerToDoSomething().sceneBrain.reactToScene(scene!!, this)
    }

    private fun addScene(player: ShortStateCharacter, creator: SceneMaker?){
        println("scene($creator) added for $player, who has ${player.energy} energy left")
        if(creator == null){
            println("OHH NO we need a new scene!!!!")
        }
        if(scene == null){
            val toAdd = creator!!.makeScene(this)
            if(toAdd != null){
                scene = toAdd
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
        if(this.scene == scene){
            println("scene ended")
            scene!!.characters.forEach { player -> player.energy -= 1}
            //if there was a conversation, characters might have learned something in this time
            if(scene!!.conversation != null){
                scene!!.characters.filter { character -> character.player.npc }.forEach { character -> character.player.brain.thinkAboutNextTurn(this.game) }
            }
            this.scene = null
        }
        if(scene.characters.filter { char -> !char.player.npc }.isNotEmpty()){
            Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        }
    }
}