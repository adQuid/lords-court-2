package game.action

import game.Game
import game.GameCharacter
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Describable
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

abstract class Action: Displayable, Describable {

    companion object{
        fun baseActionPane(action: Action, parent: MutableSet<Action>?): GridPane {
            val root = GridPane()

            val title = UtilityComponentFactory.shortWideLabel(action.tooltip(UIGlobals.playingAs()))
            (title.children[1] as Label).font = Font(20.0)

            root.add(title,0,0)
            root.add(UtilityComponentFactory.shortWideLabel(action.description()),0,1)
            if(parent != null){
                root.add(UtilityComponentFactory.shortWideButton("Remove", EventHandler { parent.remove(action); UIGlobals.defocus() }), 0, 9)
            }
            root.add(UtilityComponentFactory.backButton(),0,10)

            root.setPrefSize(UIGlobals.totalWidth(), UIGlobals.totalHeight())
            return root
        }
    }

    abstract fun isLegal(game: Game, player: GameCharacter): Boolean

    abstract fun doAction(game: Game, player: GameCharacter)

    abstract fun saveString(): Map<String, Any>

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(baseActionPane(this, null))
    }

    override fun hashCode(): Int {
        return 0 //So that sets are always comparing based on equals. There should never be huge collections of actions anyway
    }

    abstract fun collidesWith(other: Action): Boolean

    open fun actionPane(action: Action, parent: MutableSet<Action>?): GridPane{
        return baseActionPane(action, parent)
    }
}

class ActionComponentFactory: Displayable{
        val action: Action
        val parent: MutableSet<Action>

        constructor(action: Action, parent: MutableSet<Action>){
            this.action = action
            this.parent = parent
        }

        override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(action.actionPane(action, parent))
    }

}