package ui.componentfactory

import aibrain.Deal
import aibrain.UnfinishedDeal
import game.GameCharacter
import game.action.Action
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.commoncomponents.AppendableList
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import javafx.scene.input.MouseEvent


open class DealComponentFactory {

    val deal: Deal
    var actions: MutableMap<GameCharacter, MutableSet<Action>>
    var currentPage: GameCharacter
    var actionLists: MutableMap<GameCharacter, AppendableList>

    constructor(deal: Deal){
        this.deal = deal
        this.actions = deal.theActions().mapValues { entry -> entry.value.toMutableSet() }.toMutableMap()
        currentPage = actions.keys.first()
        actionLists = actions.entries.associate { entry -> entry.key to AppendableList() }.toMutableMap()
    }

    private fun resetActions(){
        actions = deal.theActions().mapValues { entry -> entry.value.toMutableSet() }.toMutableMap()
        actionLists = actions.entries.associate { entry -> entry.key to AppendableList() }.toMutableMap()
    }

    private fun action(): ActionChooser{
        return ActionChooser(currentPage, actions.keys.toList(),
            {action -> actions[currentPage]!!.add(action); UIGlobals.defocus()})
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(topBar(),0,0)
        root.add(actionList(),0,1)

        val bottomPane = GridPane()
        if(deal is UnfinishedDeal){
            bottomPane.add(UtilityComponentFactory.shortButton("Cancel", EventHandler { resetActions(); UIGlobals.defocus()}),0,0)
            bottomPane.add(UtilityComponentFactory.shortButton("Complete", EventHandler { deal.actions = actions ; UIGlobals.defocus()}, 3),1,0)
        } else {
            bottomPane.add(UtilityComponentFactory.backButton(), 0,0)
        }

        root.add(bottomPane, 0,2)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }

    private fun topBar(): Pane {
        val topPane = GridPane()
        var index = 0
        actions.forEach {
            val topic = UtilityComponentFactory.proportionalButton("${it.key.name} will...", tapClickAction(it.key), (actions.size+1).toDouble())
            if(it.key == currentPage){
                topic.font = Font(18.0)
                topic.onAction = null
            }
            topic.setMinSize(UIGlobals.totalWidth() / (deal.theActions().size+1), UIGlobals.totalHeight() / 12)
            topPane.add(topic, index++, 0)
        }
        if(deal is UnfinishedDeal){
            topPane.add(UtilityComponentFactory.proportionalButton("Add Character", EventHandler { _ -> UIGlobals.focusOn(characterSelector()) }, (actions.size+1).toDouble()),index++,0)
        }
        return topPane
    }

    private fun tapClickAction(tab: GameCharacter): EventHandler<MouseEvent>{
        return EventHandler { event ->
            if((event as MouseEvent).button == MouseButton.SECONDARY){
                (deal as UnfinishedDeal).actions.remove(tab)
                actions = actions.filter { entry -> (entry.key != tab)}.toMutableMap()
                actionLists = actionLists.filter { entry -> actions.containsKey(entry.key) }.toMutableMap()
                if(actionLists[currentPage] == null){
                    currentPage = actionLists.keys.first()
                }
            } else{
                currentPage = tab
            }
            UIGlobals.refresh()
        }
    }

    private fun actionList(): Pane {
        if(deal is UnfinishedDeal){
            return actionLists[currentPage]!!.actionList(actions[currentPage]!!, action())
        } else {
            return actionLists[currentPage]!!.actionList(actions[currentPage]!!, null)
        }
    }

    private fun characterSelector(): SelectionModal<GameCharacter> {
        val tabs = listOf(
            Tab<GameCharacter>(
                "Characters",
                UIGlobals.activeGame().players.toList()
            )
        )
        val selectModal = SelectionModal("Select Character",
            tabs,
            { player -> addCharacterToDeal(player); currentPage = player; UIGlobals.defocus() })
        return selectModal
    }

    private fun addCharacterToDeal(character: GameCharacter){
        (deal as UnfinishedDeal).actions[character] = mutableSetOf()
        actions[character] = mutableSetOf()
        actionLists[character] = AppendableList()
    }
}