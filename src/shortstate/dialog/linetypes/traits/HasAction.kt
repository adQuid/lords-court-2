package shortstate.dialog.linetypes.traits

import game.action.Action

interface HasAction {
    fun mySetAction(action: Action)

    fun myGetAction(): Action?
}