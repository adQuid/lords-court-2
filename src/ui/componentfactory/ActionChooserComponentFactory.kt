package ui.componentfactory

import game.GameCharacter
import game.action.Action
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.totalHeight
import ui.totalWidth

class ActionChooserComponentFactory {

    val player: GameCharacter

    constructor(player: GameCharacter){
        this.player = player
    }

    fun scenePage(perspective: ShortStateCharacter, action: (Action.ActionType) -> Unit): Scene {
        val root = GridPane()

        println(player.actionsEntitled())
        root.add(UtilityComponentFactory.basicList(player.actionsEntitled(), action, totalWidth, (5* totalHeight)/6), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, totalWidth, totalHeight)
    }
}

//dummy model object when choosing an action. May need to make this a full object later.
class ActionChooser: Displayable {
    val player: GameCharacter
    val callback: (Action.ActionType) -> Unit

    constructor(player: GameCharacter, action: (Action.ActionType) -> Unit){
        this.player = player
        this.callback = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return ActionChooserComponentFactory(player).scenePage(perspective, callback)
    }
}