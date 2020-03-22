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
import main.Controller
import shortstate.ShortStateCharacter
import ui.commoncomponents.AppendableList
import ui.totalHeight
import ui.totalWidth

open class DealComponentFactory {

    val deal: Deal
    var currentPage: GameCharacter
    val actionLists: Map<GameCharacter, AppendableList<Action>>

    constructor(deal: Deal){
        this.deal = deal
        currentPage = deal.theActions().keys.first()
        actionLists = deal.theActions().entries.associate { entry -> entry.key to AppendableList<Action>() }
    }

    private fun action(): ActionChooser{
        return ActionChooser(currentPage, (deal as UnfinishedDeal).actions.keys.toList(),
            {action -> deal.actions[currentPage]!!.add(Action(action)); Controller.singleton!!.GUI!!.defocus()})
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(topBar(),0,0)
        root.add(actionList(),0,1)
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

    private fun actionList(): Pane {
        if(deal is UnfinishedDeal){
            return actionLists[currentPage]!!.actionList(deal.actions[currentPage]!!, action())
        } else {
            return actionLists[currentPage]!!.actionList(deal.theActions()[currentPage]!!, null)
        }
    }
}