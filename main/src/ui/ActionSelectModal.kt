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


class ActionSelectModal {

    val parent: MainUI
    val closeAction: (Action) -> Unit

    constructor(parent: MainUI, action: (Action) -> Unit){
        this.parent = parent
        this.closeAction = action
    }

    fun getScene(): Scene {
        val pane = GridPane()

        val topPane = GridPane()
        val topic1 = Button("General Actions")
        topic1.setMinSize(parent.totalWidth / 2, parent.totalHeight / 12)
        val topic2 = Button("Drafted Actions")
        topic2.setMinSize(parent.totalWidth / 2, parent.totalHeight / 12)
        topPane.add(topic1, 0, 0)
        topPane.add(topic2,1,0)

        val data = FXCollections.observableArrayList<Action>()
        data.addAll(Controller.singleton!!.game!!.possibleActionsForPlayer(parent.playingAs()))
        val listView = ListView<Action>(data)
        listView.items = data
        listView.setPrefSize(parent.totalWidth,parent.totalHeight * (5.0/6.0))
        listView.setCellFactory({ _: ListView<Action> -> ActionPickCell(closeAction) })

        val btn8 = parent.generalComponents.makeShortWideButton("Cancel", EventHandler { parent.focusOn(parent.lineBeingConstructed)})
        pane.add(topPane,0,0)
        pane.add(listView,0,1)
        pane.add(btn8, 0, 2)
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