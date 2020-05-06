package ui.specialdisplayables.selectionmodal.modals

import game.action.Action
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.control.ListCell
import javafx.scene.text.Text
import shortstate.room.Room
import ui.MainUI
import ui.componentfactory.UtilityComponentFactory


class RoomSelectModal {

    val parent: MainUI
    val closeAction: (Room) -> Unit

    constructor(parent: MainUI, action: (Room) -> Unit): super(){
        this.parent = parent
        this.closeAction = action
    }

    fun getScene(): Scene {
        val pane = GridPane()

        for((index, room) in parent.playingAs()!!.player.location.rooms.withIndex()){
            pane.add(
                UtilityComponentFactory.shortWideButton("room", EventHandler { closeAction(room) }), 0,
                index
            )
        }
        return Scene(pane, parent.totalWidth, parent.totalHeight)
    }

    internal class ActionPickCell(val closeAction: (Action) -> Unit) : ListCell<Action>() {

        public override fun updateItem(item: Action?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                this.graphic = Text(item.toString())
                this.onMouseClicked = EventHandler{_ -> closeAction(item)}
            }
        }
    }
}