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
        fun defaultActionPane(action: Action): GridPane {
            val retval = baseActionPane(action)
            retval.add(UtilityComponentFactory.proportionalLabel(action.description(), 1.0, 0.8),0,1)
            return retval
        }

        fun baseActionPane(action: Action): GridPane {
            val root = GridPane()

            val title = UtilityComponentFactory.shortWideLabel(action.tooltip(UIGlobals.playingAs()))
            (title.children[1] as Label).font = Font(20.0)

            root.add(title,0,0)
            root.add(UtilityComponentFactory.backButton(),0,10)

            root.setPrefSize(UIGlobals.totalWidth(), UIGlobals.totalHeight())
            return root
        }
    }

    abstract fun isLegal(game: Game, player: GameCharacter): Boolean

    abstract fun doAction(game: Game, player: GameCharacter)

    abstract fun saveString(): Map<String, Any>

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(defaultActionPane(this))
    }

    override fun hashCode(): Int {
        return 0 //So that sets are always comparing based on equals. There should never be huge collections of actions anyway
    }

    abstract fun collidesWith(other: Action): Boolean

    open fun actionPane(action: Action): GridPane{
        return defaultActionPane(action)
    }
}

class ActionComponentFactory: Displayable{
        val action: Action

        constructor(action: Action){
            this.action = action
        }

        override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(action.actionPane(action))
    }

}