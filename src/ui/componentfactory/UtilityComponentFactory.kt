package ui.componentfactory

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab
import ui.totalHeight
import ui.totalWidth


object UtilityComponentFactory {

    fun imageView(url: String): ImageView {
        val image = Image(url)
        val retval = ImageView(image)
        retval.fitHeight = (totalHeight) * (4.5 / 6.0)
        retval.fitWidth = totalWidth
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
        statsDisplay.setMinSize(totalWidth/2, totalWidth / 12)
        retval.add(statsDisplay, 0,0)

        val saveButton = shortButton("Save", EventHandler { Controller.singleton!!.save()})
        retval.add(saveButton, 1,0)

        val loadButton = shortButton("Load", EventHandler { Controller.singleton!!.load(); Controller.singleton!!.GUI!!.resetFocus()})
        retval.add(loadButton, 2,0)

        return retval
    }

    fun newSceneButton(perspective: ShortStateCharacter): Button {
        return shortButton("Go Somewhere Else",
            EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(
                SelectionModal(Controller.singleton!!.GUI!!, newSceneOptions(perspective), { maker -> goToNewSceneIfApplicable(maker, perspective)})
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
        Controller.singleton!!.GUI!!.resetFocus()
    }

    fun shortButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(totalWidth / 4, totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

    fun shortWideButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(totalWidth, totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

}