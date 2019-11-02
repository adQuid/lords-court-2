package shortstate

import game.Game
import game.Location
import game.Character
import main.Controller
import shortstate.room.action.GoToBed
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class ShortStateGame {

    private var scene: Scene? = null
    val game: Game
    val location: Location
    val players: List<ShortStateCharacter>

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStateCharacter(player) }
        establishStartingScene()
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

    }

    private fun establishStartingScene(){
        players.forEach { player ->
            player.nextSceneIWannaBeIn = GoToRoomSoloMaker(player,location.startRoom())
        }
        //this SHOULD be safe, since the game just started. this will crash on an empty location
        addScene(nextActingPlayer()!!, nextActingPlayer()!!.nextSceneIWannaBeIn)
    }

    fun doAIIfAppropriate(){
        if(scene != null){
            if(scene!!.conversation != null){
                val convo = scene!!.conversation!!
                if(scene!!.conversation!!.age > 5){
                    endScene(scene!!)
                } else {
                    if(convo.otherParticipant(convo.lastSpeaker).player.npc){
                        convo.submitLine(convo.otherParticipant(convo.lastSpeaker).convoBrain.reactToLine(convo.lastLine, convo.lastSpeaker.player, game), this)
                    }
                }
            } else {
                //TODO: Have this do other things
                if(scene!!.characters[0].player.npc){
                    if(scene!!.characters[0].energy < 990){
                        GoToBed().doAction(this, scene!!.characters[0])
                    }
                    endScene(scene!!)
                }
            }
        }
    }

    fun endScene(scene: Scene){
        if(this.scene == scene){
            println("scene ended")
            scene!!.characters.forEach { player -> player.energy -= 1}
            this.scene = null

            val nextPlayer = nextActingPlayer()

            if(nextPlayer == null){
                println("no players left to act. Doing nothing else")
            } else {

                if(nextPlayer.player.npc && nextPlayer.nextSceneIWannaBeIn == null){
                    nextPlayer.decideNextScene(this)
                }
                addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)
                nextPlayer.nextSceneIWannaBeIn = null
                doAIIfAppropriate()
            }

            Controller.singleton!!.GUI!!.refocus()
        }
    }
}