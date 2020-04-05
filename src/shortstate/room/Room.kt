package shortstate.room

import game.GameCharacter
import shortstate.GameRules
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.action.*

class Room {

    val NAME_NAME = "NAME"
    val name: String
    val TYPE_NAME = "TYPE"
    val type: RoomType
    val PICTURE_TEXT_NAME = "PICTURE"
    val pictureText: String

    constructor(name: String, pictureText: String, type: RoomType){
        this.name = name
        this.type = type
        this.pictureText = pictureText
    }

    constructor(parent: ShortStateGame, saveString: Map<String, Any>){
        name = saveString[NAME_NAME] as String
        type = RoomType.valueOf(saveString[TYPE_NAME] as String)
        pictureText = saveString[PICTURE_TEXT_NAME] as String
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            NAME_NAME to name,
            TYPE_NAME to type.toString(),
            PICTURE_TEXT_NAME to pictureText
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

    fun getActions(player: ShortStateCharacter): List<RoomAction>{
        if(type == RoomType.WORKROOM){
            var retval = baseActions()
                .plus(player.player.titles.flatMap { title -> title.reportsEntitled.map { type -> MakeReport(type)} })

            if(player.energy >= GameRules.COST_TO_MAKE_WRIT){
                retval = retval.plus(listOf(DraftWrit()))
            }
            return retval
        } else if(type == RoomType.THRONEROOM){
            return baseActions()
                .plus(player.player.writs.map { writ -> EnactWrit(writ) })
        } else if(type == RoomType.BEDROOM){
            return baseActions()
                .plus(listOf(GoToBed()))
        } else {
            return baseActions()
        }
    }

    private fun baseActions(): List<RoomAction>{
        return listOf(Wait())
    }

    enum class RoomType{
        BEDROOM, WORKROOM, THRONEROOM, ETC
    }

}