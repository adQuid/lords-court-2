package shortstate.dialog.linetypes

import game.action.Action

interface HasAction {
    fun mySetAction(action: Action)

    fun myGetAction(): Action?
}