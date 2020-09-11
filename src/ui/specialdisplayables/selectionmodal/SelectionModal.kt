package ui.specialdisplayables.selectionmodal

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.control.ListCell
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Describable
import ui.NoPerspectiveDisplayable
import ui.PerspectiveDisplayable
import ui.commoncomponents.PrettyPrintable
import ui.componentfactory.UtilityComponentFactory


class SelectionModal<T>: NoPerspectiveDisplayable {

    val parent = UIGlobals.GUI()
    val title: String
    val options: List<Tab<T>>
    val closeAction: ((T) -> Unit)?

    var focusedTab: Tab<T>

    constructor(title: String, options: List<Tab<T>>, action: ((T) -> Unit)?): super(){
        this.title = title
        this.options = options
        this.focusedTab = options[0]
        this.closeAction = action
    }

    override fun display(): Scene {
        return getScene()
    }

    fun getScene(): Scene {
        val pane = GridPane()

        val data = FXCollections.observableArrayList<T>()
        data.addAll(focusedTab.items)

        val btn8 = UtilityComponentFactory.shortWideButton("Cancel", EventHandler { parent.defocus()})
        pane.add(topPane(),0,0)
        if(data.isEmpty()){
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,1)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,2)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,3)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,4)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,5)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,6)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,7)
            pane.add(UtilityComponentFactory.shortWideLabel(""),0,8)
        } else {
            val listView = ListView<T>(data)
            listView.style = "-fx-focus-color:rgba(0,0,0,0.0);  -fx-padding: 0;"
            listView.setPrefSize(parent.totalWidth,parent.totalHeight * 0.8)
            listView.setCellFactory({ _: ListView<T> ->
                ActionPickCell(
                    closeAction
                )
            })

            pane.add(listView,0,1)
        }
        pane.add(btn8, 0, 9)

        return Scene(pane, parent.totalWidth, parent.totalHeight)
    }

    private fun topPane(): Pane {
        val retval = GridPane()
        retval.style = "-fx-background-color: tan"

        if(options.size > 1){
            val titlePane = GridPane()
            titlePane.add(UtilityComponentFactory.extraShortWideLabel(
                title), 0,0)
            retval.add(titlePane, 0, 0)

            val tabsPane = GridPane()
            var index = 0
            options.forEach {
                val topic = UtilityComponentFactory.proportionalButton(it.title, EventHandler{_ -> focusedTab = it; parent.display()}, options.size * 1.0, 0.05, it == focusedTab)

                tabsPane.add(topic, index++, 0)
            }
            retval.add(tabsPane, 0,1)
        } else {
            retval.add(UtilityComponentFactory.shortWideLabel(title), 0,0)
        }
        retval.requestFocus()
        return retval
    }

    internal class ActionPickCell<T>(val closeAction: ((T) -> Unit)?) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                val displayText =
                if(item is PrettyPrintable){
                    item.prettyPrint(UIGlobals.GUI().shortGame(), UIGlobals.playingAs())
                } else if(item is Describable){
                    item.tooltip(UIGlobals.playingAs())
                }else {
                    item.toString()
                }

                if(closeAction != null){
                    this.graphic = UtilityComponentFactory.shortWideButton(displayText, EventHandler { })
                    this.onMouseClicked = EventHandler { _ -> this.graphic = UtilityComponentFactory.shortWideClickedButton(displayText, EventHandler { });  Platform.runLater { closeAction!!(item) }}
                    this.padding = Insets.EMPTY
                } else {
                    val node = GridPane()

                    node.add(UtilityComponentFactory.shortWideLabel(displayText),0,0)

                    this.graphic = node
                }
            } else {
                this.graphic = UtilityComponentFactory.shortWideLabel("")
                this.padding = Insets.EMPTY
            }
        }
    }
}