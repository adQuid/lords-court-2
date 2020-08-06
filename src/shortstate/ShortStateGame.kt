package shortstate

import game.Game
import game.Location
import game.GameCharacter

class ShortStateGame {

    val SCENE_NAME = "SCENE"
    var shortGameScene: ShortGameScene? = null
    val game: Game
    val LOCATION_NAME = "LOCATION"
    val location: Location
    val PLAYERS_NAME = "PLAYERS"
    val players: List<ShortStateCharacter>
    var stopped = false

    constructor(game: Game, location: Location){
        this.game = game
        this.location = location
        this.players = game.playersAtLocation(location).map { player -> ShortStateCharacter(player) }

    }

    constructor(parent: Game, saveString: Map<String,Any?>){
        game = parent
        location = parent.locationById(saveString[LOCATION_NAME] as Int)
        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> ShortStateCharacter(parent, map) }
        if(saveString[SCENE_NAME] != null){
            shortGameScene = ShortGameScene(this, saveString[SCENE_NAME] as Map<String, Any>)
        }
    }

    fun saveString(): Map<String, Any?>{
        return hashMapOf(
            SCENE_NAME to {if(shortGameScene != null){ shortGameScene!!.saveString() }else{ null}}(),
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
            if(it.player.npc && !it.done && it.nextSceneIWannaBeIn == null){
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

    fun shortPlayerForLongPlayer(player: GameCharacter): ShortStateCharacter?{
        players.forEach {
            if(it.player == player){
                return it
            }
        }
        return null
    }

    fun nextActingPlayer(): ShortStateCharacter?{
        if(stopped){
            return null
        }
        //debug
        if(players.filter { !it.done }.sortedByDescending { it.energy }.getOrNull(0) != null){
            //println( players.filter { !it.done }.sortedByDescending { it.energy }.getOrNull(0)!!.player.name +" is next")
        }
        return players.filter { !it.done }.sortedByDescending { it.energy }.getOrNull(0)
    }

    fun sceneForPlayer(player: ShortStateCharacter): ShortGameScene?{
        if(shortGameScene != null && shortGameScene!!.characters!!.contains(player)){
            return shortGameScene
        }

        return null
    }


}