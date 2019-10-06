package shortstate

import game.Game
import game.Location
import shortstate.scenemaker.SceneMaker

class ShortStateGame {

    private val scenes: MutableList<Scene> = mutableListOf()
    val game: Game
    val location: Location
    val players: List<ShortStatePlayer>

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStatePlayer(player) }
        establishStartingScenes()
    }

    @Synchronized fun nextPlayerToDoShortStateStuff(): ShortStatePlayer?{
        players.sortedByDescending { player -> player.energy }.forEach {
            if(it.player.npc && it.energy > 0){
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

    fun sceneForPlayer(player: ShortStatePlayer): Scene?{
        scenes.forEach {
            if(it.characters.contains(player)){
                return it
            }
        }
        return null
    }

    fun addScene(creator: SceneMaker): Scene?{
        val toAdd = creator.makeScene(this)
        if(toAdd != null){
            scenes.add(toAdd)
        }
        return toAdd
    }

    private fun establishStartingScenes(){
        players.forEach { player ->
            scenes.add(Scene(listOf(player),location.startRoom(), null))
        }
    }

    fun endScene(scene: Scene){
        scenes.remove(scene)
    }
}