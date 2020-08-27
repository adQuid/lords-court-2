package ui.componentfactory

import game.GameCharacter
import game.action.Action
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable

class ActionChooserComponentFactory {

    val player: GameCharacter
    val context: List<GameCharacter>

    constructor(player: GameCharacter, context: List<GameCharacter>){
        this.player = player
        this.context = context
    }

    fun scenePage(perspective: ShortStateCharacter, action: (Action) -> Unit): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.basicList(player.actionsReguarding(context), action, null, UIGlobals.totalWidth(), UIGlobals.totalHeight() * 0.9), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
    }
}

//dummy model object when choosing an action. May need to make this a full object later.
class ActionChooser: PerspectiveDisplayable {
    val player: GameCharacter
    val context: List<GameCharacter>
    val callback: (Action) -> Unit

    constructor(player: GameCharacter, context: List<GameCharacter>, action: (Action) -> Unit){
        this.player = player
        this.context = context
        this.callback = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return ActionChooserComponentFactory(player, context).scenePage(perspective, callback)
    }
}