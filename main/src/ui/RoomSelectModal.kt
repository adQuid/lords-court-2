package ui

import action.Action
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.control.ListCell
import javafx.scene.text.Text
import main.Controller
import shortstate.Room


class RoomSelectModal {

    val parent: MainUI
    val closeAction: (Room) -> Unit

    constructor(parent: MainUI, action: (Room) -> Unit){
        this.parent = parent
        this.closeAction = action
    }

    fun getScene(): Scene {
        val pane = GridPane()

        for((index, room) in parent.playingAs().location.rooms.withIndex()){
            pane.add(parent.generalComponents.makeShortWideButton("room", EventHandler { closeAction(room) }), 0,
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