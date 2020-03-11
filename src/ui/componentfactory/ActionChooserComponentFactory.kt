package ui.componentfactory

import game.action.Action
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.totalHeight
import ui.totalWidth

class ActionChooserComponentFactory {

    fun scenePage(perspective: ShortStateCharacter, action: (Action.ActionType) -> Unit): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.basicList(perspective.player.actionsEntitled(), action, totalWidth, (5* totalHeight)/6), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, totalWidth, totalHeight)
    }
}

//dummy model object when choosing an action. May need to make this a full object later.
class ActionChooser: Displayable {
    val action: (Action.ActionType) -> Unit

    constructor(action: (Action.ActionType) -> Unit){
        this.action = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return ActionChooserComponentFactory().scenePage(perspective, action)
    }
}