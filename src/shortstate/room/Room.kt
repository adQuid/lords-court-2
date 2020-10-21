package shortstate.room

import gamelogic.territory.TerritoryLogicModule
import main.UIGlobals
import shortstate.GameRules
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.action.*
import shortstate.room.actionmaker.DefaultRoomActionMaker
import shortstate.room.actionmaker.DraftWritRoomActionMaker

class Room {

    companion object{
        val NAME_NAME = "NAME"
        val TYPE_NAME = "TYPE"
        val PICTURE_TEXT_NAME = "PICTURE"
        val tooltips = mapOf(
            RoomType.BEDROOM to "Take a nap",
            RoomType.WORKROOM to "Gather reports, draft new writs, or view your lands",
            RoomType.ETC to "",
            RoomType.THRONEROOM to "announce your writs to enact them into law"
        )
        val sounds = mapOf(
            RoomType.WORKROOM to "paper.mp3"
        )
    }


    val name: String
    val type: RoomType
    val imagePath: String

    constructor(name: String, pictureText: String, type: RoomType){
        this.name = name
        this.type = type
        this.imagePath = pictureText
    }

    constructor(parent: ShortStateGame, saveString: Map<String, Any>){
        name = saveString[NAME_NAME] as String
        type = RoomType.valueOf(saveString[TYPE_NAME] as String)
        imagePath = saveString[PICTURE_TEXT_NAME] as String
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            NAME_NAME to name,
            TYPE_NAME to type.toString(),
            PICTURE_TEXT_NAME to imagePath
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is Room){
            return this.name == other.name &&
            this.type == other.type
        } else {
            return false
        }
    }

    override fun toString(): String {
        return name
    }

    fun getActions(player: ShortStateCharacter): List<RoomActionMaker>{
        if(type == RoomType.WORKROOM){
            var retval = baseActions()

            if(player.energy >= GameRules.COST_TO_MAKE_WRIT){
                retval = retval.plus(listOf(DraftWritRoomActionMaker()))
            }

            //TODO: Move this somewhere else
            if(!player.player.npc && UIGlobals.activeGame().gameLogicModules.filter { it.type == TerritoryLogicModule.type }.isNotEmpty()){
                retval = retval.plus(DefaultRoomActionMaker(ViewEstates()))
            }
            return retval
        } else if(type == RoomType.THRONEROOM){
            return baseActions()
                .plus(player.player.writs.filter{it.complete()}.map { writ -> DefaultRoomActionMaker(EnactWrit(writ)) })
        } else if(type == RoomType.BEDROOM){
            return baseActions()
        } else {
            return baseActions()
        }
    }

    private fun baseActions(): List<RoomActionMaker>{
        return listOf(DefaultRoomActionMaker(Wait()))
    }

    enum class RoomType{
        BEDROOM, WORKROOM, THRONEROOM, ETC
    }

}