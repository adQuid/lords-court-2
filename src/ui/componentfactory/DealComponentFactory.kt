package ui.componentfactory

import aibrain.Deal
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

class DealComponentFactory {

    val deal: Deal
    var currentPage: GameCharacter

    constructor(deal: Deal){
        this.deal = deal
        currentPage = deal.actions.keys.first()
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(topBar(),0,0)
        root.add(actionList(deal.actions[currentPage]!!),0,1)
        root.add(UtilityComponentFactory.shortWideButton("Back", EventHandler { Controller.singleton!!.GUI!!.defocus()}),0,2)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    private fun topBar(): Pane {
        val topPane = GridPane()
        var index = 0
        deal.actions.forEach {
            val topic = Button("${it.key.name} will...")
            if(it.key == currentPage){
                topic.font = Font(18.0)
            } else {
                topic.onAction = EventHandler {_ -> currentPage = it.key; Controller.singleton!!.GUI!!.display()}
            }
            topic.setMinSize(totalWidth / deal.actions.size, totalHeight / 12)
            topPane.add(topic, index++, 0)
        }
        return topPane
    }

    private fun actionList(actions: List<Action>): Pane {
        val retval = GridPane()
        for((index,action) in actions.withIndex()){
            val toAdd = Text(action.toString())
            toAdd.font = Font(20.0)
            retval.add(toAdd, 0, index)
        }
        retval.setPrefSize(totalWidth, (5* totalHeight)/6)
        return retval
    }

}