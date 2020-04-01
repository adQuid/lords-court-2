package ui.componentfactory

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.specialdisplayables.OptionsMenu
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab


object UtilityComponentFactory {

    fun imageView(url: String): ImageView {
        val path = url.replace("//","\\")
        val image = Image(path)
        val retval = ImageView(image)
        retval.fitHeight = (UIGlobals.totalHeight()) * (4.5 / 6.0)
        retval.fitWidth = UIGlobals.totalWidth()
        return retval
    }

    fun bottomPane(buttons: List<Button>, perspective: ShortStateCharacter): Pane{

        val buttonsPane = GridPane()
        for(index in 0..7){
            if(index < buttons.size){
                buttonsPane.add(buttons[index], (index % 4), if(index > 3) 1 else 0)
            } else {
                buttonsPane.add(shortButton("", null), (index % 4), if(index > 3) 1 else 0)
            }
        }

        val retval = GridPane()
        retval.add(middlePane(perspective), 0, 0)
        retval.add(buttonsPane, 0,1)

        return retval
    }

    private fun middlePane(perspective: ShortStateCharacter): Pane{
        val retval = GridPane()

        val statsDisplay = Label("Energy: " + perspective.energy + "/1000")
        statsDisplay.setMinSize(UIGlobals.totalWidth()/2, UIGlobals.totalWidth() / 12)
        retval.add(statsDisplay, 0,0)

        val optionsButton = UtilityComponentFactory.shortButton("Options", EventHandler { UIGlobals.focusOn(OptionsMenu()) })
        retval.add(optionsButton, 1,0)

        return retval
    }

    fun newSceneButton(perspective: ShortStateCharacter): Button {
        return shortButton("Go Somewhere Else",
            EventHandler { _ -> UIGlobals.GUI().focusOn(
                SelectionModal(
                    newSceneOptions(perspective),
                    { maker -> goToNewSceneIfApplicable(maker, perspective) })
            )
            }
        )
    }

    private fun newSceneOptions(perspective: ShortStateCharacter): List<Tab<SceneMaker>>{
        val goToRoomMakers = perspective.player.location.rooms.map { room -> GoToRoomSoloMaker(perspective, room) }
        val goToRoomTab = Tab<SceneMaker>("Go to Room", goToRoomMakers)

        val conversationMakers = Controller.singleton!!.shortThreadForPlayer(perspective).shortGame.players.minusElement(perspective)
            .map { player -> ConversationMaker(perspective, player,perspective.player.location.roomByType(
                Room.RoomType.ETC)) }
        val conversationTab = Tab<SceneMaker>("Conversation", conversationMakers)

        return listOf(goToRoomTab, conversationTab)
    }

    private fun goToNewSceneIfApplicable(maker: SceneMaker, perspective: ShortStateCharacter){
        perspective.nextSceneIWannaBeIn = maker
        Controller.singleton!!.shortThread!!.endScene(Controller.singleton!!.shortThreadForPlayer(perspective).shortGame.shortGameScene!!)
        UIGlobals.resetFocus()
    }

    fun backButton(): Button{
        return shortWideButton("Back", EventHandler { UIGlobals.defocus()})
    }

    fun <T> basicList(items: List<T>, onClick: (T) -> Unit, width: Double, height: Double): ListView<T> {
        val data = FXCollections.observableArrayList<T>()
        data.addAll(items)
        val listView = ListView<T>(data)
        listView.items = data
        listView.setPrefSize(width,height)
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(onClick) })

        return listView
    }

    internal class ActionPickCell<T>(val closeAction: (T) -> Unit) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                this.graphic = Text(item.toString())
                this.onMouseClicked = EventHandler{_ -> closeAction(item)}
            }
        }
    }

    fun shortButton(text: String, action: EventHandler<ActionEvent>?): Button {
        return shortButton(text, action, 1)
    }

    fun shortButton(text: String, action: EventHandler<ActionEvent>?, specialWidth: Int): Button {
        return shortButton(text, action, specialWidth.toDouble())
    }

    fun shortButton(text: String, action: EventHandler<ActionEvent>?, specialWidth: Double): Button {
        val retval = proportionalButton(text, action, 4.0)
        retval.setMinSize(retval.minWidth*specialWidth, retval.minHeight)
        return retval
    }

    fun shortWideButton(text: String, action: EventHandler<ActionEvent>?): Button {
        return proportionalButton(text, action, 1.0)
    }

    fun proportionalButton(text: String, action: EventHandler<ActionEvent>?, proportion: Double): Button{
        val retval = Button(text)
        retval.setMinSize(UIGlobals.totalWidth() / proportion, UIGlobals.totalHeight() / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }
}