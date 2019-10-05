package shortstate

import game.Conversation
import game.Game
import game.Location
import game.Player

class ShortStateGame {

    val scenes: MutableList<Scene> = mutableListOf()
    val game: Game
    val location: Location
    val players: List<ShortStatePlayer>

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStatePlayer(player) }
        establishStartingScenes()
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

    fun createConversation(initiator: ShortStatePlayer, target: ShortStatePlayer, room: Room): Scene?{
        //TODO: make sure characters aren't already in scene
        val convo = Conversation(initiator, target)
        val sceneToMake = Scene(listOf(initiator, target), room, convo)
        scenes.add(sceneToMake)
        return sceneToMake
    }

    private fun establishStartingScenes(){
        players.forEach { player ->
            scenes.add(Scene(listOf(player),location.startRoom(), null))
        }
    }
}