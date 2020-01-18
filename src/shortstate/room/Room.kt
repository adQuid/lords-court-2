package shortstate.room

import game.Character
import shortstate.report.ReportType
import shortstate.room.action.CommitToAction
import shortstate.room.action.GoToBed
import shortstate.room.action.MakeReport

class Room {

    val NAME_NAME = "NAME"
    val name: String
    val TYPE_NAME = "TYPE"
    val type: RoomType
    val PICTURE_TEXT_NAME = "PICTURE"
    val pictureText: String
    val baseActions: List<RoomAction>

    constructor(name: String, pictureText: String, type: RoomType){
        this.name = name
        this.type = type
        this.pictureText = pictureText
        if(type == RoomType.BEDROOM){
            baseActions = listOf(GoToBed())
        } else if(type == RoomType.WORKROOM){
            baseActions = listOf()
        } else {
            baseActions = listOf()
        }
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

    fun getActions(player: Character): List<RoomAction>{
        if(type == RoomType.WORKROOM){
            return baseActions
                .plus(player.titles.flatMap { title -> title.actionsEntitled.map { action -> CommitToAction(action) } })
                .plus(player.titles.flatMap { title -> title.reportsEntitled.map { type -> MakeReport(type)} })
        } else {
            return baseActions
        }
    }

    enum class RoomType{
        BEDROOM, WORKROOM, ETC
    }

}