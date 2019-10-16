package shortstate

import game.Game
import game.Location
import main.Controller
import shortstate.room.GoToBed
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class ShortStateGame {

    private var scene: Scene? = null
    val game: Game
    val location: Location
    val players: List<ShortStatePlayer>

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStatePlayer(player) }
        establishStartingScene()
    }

    @Synchronized fun nextPlayerToDoShortStateStuff(): ShortStatePlayer?{
        players.sortedByDescending { player -> player.energy }.forEach {
            if(it.player.npc && it.energy > 0 && it.nextSceneIWannaBeIn == null){
                return it
            }
        }
        return null
    }

    fun playerCharacter(): ShortStatePlayer{
        players.forEach {
            if(!it.player.npc){
                return it
            }
        }
        throw Exception("No player found!")
    }

    fun nextActingPlayer(): ShortStatePlayer{
        return players.sortedByDescending { it.energy }[0]
    }

    fun sceneForPlayer(player: ShortStatePlayer): Scene?{
        if(scene != null && scene!!.characters!!.contains(player)){
                return scene
        }

        return null
    }

    private fun addScene(player: ShortStatePlayer, creator: SceneMaker?){
        println("scene($creator) added for $player, who has ${player.energy} energy left")
        if(creator == null){
            println("OHH NO we need a new scene!!!!")
        }
        if(scene == null){
            player.energy -= 1
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
        addScene(nextActingPlayer(), nextActingPlayer().nextSceneIWannaBeIn)
    }

    private fun doAIIfAppropriate(){
        if(scene != null){
            if(scene!!.conversation != null){
                scene!!.conversation!!.doAIIfAppropriate(game)
            } else {
                //TODO: Have this do other things
                if(scene!!.characters[0].player.npc){
                    if(scene!!.characters[0].energy < 990){
                        GoToBed().doAction(this, scene!!.characters[0])
                    }
                    endScene(scene)
                }
            }
        }
    }

    fun endScene(scene: Scene?){
        if(this.scene == scene){
            this.scene = null
        }
        val nextPlayer = nextActingPlayer()
        if(nextPlayer.player.npc && nextPlayer.nextSceneIWannaBeIn == null){
            nextPlayer.decideNextScene(this)
        }
        addScene(nextPlayer, nextPlayer.nextSceneIWannaBeIn!!)
        nextPlayer.nextSceneIWannaBeIn = null
        doAIIfAppropriate()

        Controller.singleton!!.GUI!!.refocus()
    }
}