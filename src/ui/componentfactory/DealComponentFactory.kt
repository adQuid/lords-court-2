package ui.componentfactory

import aibrain.Deal
import aibrain.UnfinishedDeal
import game.GameCharacter
import game.action.Action
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller
import shortstate.ShortStateCharacter
import ui.totalHeight
import ui.totalWidth

open class DealComponentFactory {

    val deal: Deal
    var currentPage: GameCharacter

    constructor(deal: Deal){
        this.deal = deal
        currentPage = deal.theActions().keys.first()
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(topBar(),0,0)
        root.add(actionList(deal.theActions()[currentPage]!!),0,1)
        root.add(UtilityComponentFactory.backButton(),0,2)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    private fun topBar(): Pane {
        val topPane = GridPane()
        var index = 0
        deal.theActions().forEach {
            val topic = Button("${it.key.name} will...")
            if(it.key == currentPage){
                topic.font = Font(18.0)
            } else {
                topic.onAction = EventHandler {_ -> currentPage = it.key; Controller.singleton!!.GUI!!.display()}
            }
            topic.setMinSize(totalWidth / deal.theActions().size, totalHeight / 12)
            topPane.add(topic, index++, 0)
        }
        return topPane
    }

    private fun actionList(actions: List<Action>): Pane {
        val retval = GridPane()

        if(deal is UnfinishedDeal){
            retval.add(UtilityComponentFactory.basicList(actions, {action -> (deal as UnfinishedDeal).actions[currentPage]!!.remove(action);
                Controller.singleton!!.GUI!!.display()}, totalWidth,totalHeight * (5.0/6.0)), 0, 0)

            val newActionButton = UtilityComponentFactory.shortWideButton("Add Action",
                EventHandler {_ -> Controller.singleton!!.GUI!!.focusOn(ActionChooser(currentPage, deal.actions.keys.toList(),
                    {action -> deal.actions[currentPage]!!.add(Action(action)); Controller.singleton!!.GUI!!.defocus()}))})
            retval.add(newActionButton, 0 , 1)
        } else {
            retval.add(Text("dummy"),0,1)
        }

        retval.setPrefSize(totalWidth, (5* totalHeight)/6)
        return retval
    }
}