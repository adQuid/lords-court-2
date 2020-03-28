package ui.componentfactory

import aibrain.Deal
import aibrain.UnfinishedDeal
import game.Game
import game.GameCharacter
import game.action.Action
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.commoncomponents.AppendableList
import ui.commoncomponents.selectionmodal.SelectionModal
import ui.commoncomponents.selectionmodal.Tab
import ui.contructorobjects.CharacterSelector
import ui.totalHeight
import ui.totalWidth

open class DealComponentFactory {

    val deal: Deal
    var actions: MutableMap<GameCharacter, MutableList<Action>>
    var currentPage: GameCharacter
    var actionLists: MutableMap<GameCharacter, AppendableList<Action>>

    constructor(deal: Deal){
        this.deal = deal
        this.actions = deal.theActions().mapValues { entry -> entry.value.toMutableList() }.toMutableMap()
        currentPage = actions.keys.first()
        actionLists = actions.entries.associate { entry -> entry.key to AppendableList<Action>() }.toMutableMap()
    }

    private fun resetActions(){
        actions = deal.theActions().mapValues { entry -> entry.value.toMutableList() }.toMutableMap()
        actionLists = actions.entries.associate { entry -> entry.key to AppendableList<Action>() }.toMutableMap()
    }

    private fun action(): ActionChooser{
        return ActionChooser(currentPage, actions.keys.toList(),
            {action -> actions[currentPage]!!.add(Action(action)); UIGlobals.defocus()})
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(topBar(),0,0)
        root.add(actionList(),0,1)

        val bottomPane = GridPane()
        if(deal is UnfinishedDeal){
            bottomPane.add(UtilityComponentFactory.shortButton("Cancel", EventHandler { resetActions(); UIGlobals.defocus()}),0,0)
            bottomPane.add(UtilityComponentFactory.shortButton("Complete", EventHandler { deal.actions.entries.forEach { deal.actions[it.key] = actions[it.key]!! }; UIGlobals.defocus()}, 3),1,0)
        } else {
            bottomPane.add(UtilityComponentFactory.backButton(), 0,0)
        }

        root.add(bottomPane, 0,2)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    private fun topBar(): Pane {
        val topPane = GridPane()
        var index = 0
        actions.forEach {
            val topic = UtilityComponentFactory.proportionalButton("${it.key.name} will...", EventHandler {_ -> currentPage = it.key; UIGlobals.refresh()}, (actions.size+1).toDouble())
            if(it.key == currentPage){
                topic.font = Font(18.0)
                topic.onAction = null
            }
            topic.setMinSize(totalWidth / (deal.theActions().size+1), totalHeight / 12)
            topPane.add(topic, index++, 0)
        }
        topPane.add(UtilityComponentFactory.proportionalButton("Add Character", EventHandler { _ -> UIGlobals.focusOn(characterSelector()) }, (actions.size+1).toDouble()),index++,0)
        return topPane
    }

    private fun actionList(): Pane {
        if(deal is UnfinishedDeal){
            return actionLists[currentPage]!!.actionList(actions[currentPage]!!, action())
        } else {
            return actionLists[currentPage]!!.actionList(actions[currentPage]!!, null)
        }
    }

    private fun characterSelector(): SelectionModal<GameCharacter>{
        val tabs = listOf(Tab<GameCharacter>("Characters", UIGlobals.activeGame().players.toList()))
        val selectModal = SelectionModal(tabs, {player -> addCharacterToDeal(player); UIGlobals.defocus()})
        return selectModal
    }

    private fun addCharacterToDeal(character: GameCharacter){
        (deal as UnfinishedDeal).actions[character] = mutableListOf()
        actions[character] = mutableListOf<Action>()
        actionLists[character] = AppendableList()
    }
}