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

    val players: List<GameCharacter>

    constructor(player: List<GameCharacter>){
        this.players = player
    }

    fun scenePage(perspective: ShortStateCharacter, action: (Action.ActionType) -> Unit): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.basicList(perspective.player.actionsReguarding(players), action, totalWidth, (5* totalHeight)/6), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, totalWidth, totalHeight)
    }
}

//dummy model object when choosing an action. May need to make this a full object later.
class ActionChooser: Displayable {
    val players: List<GameCharacter>
    val callback: (Action.ActionType) -> Unit

    constructor(players: List<GameCharacter>, action: (Action.ActionType) -> Unit){
        this.players = players
        this.callback = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return ActionChooserComponentFactory(players).scenePage(perspective, callback)
    }
}