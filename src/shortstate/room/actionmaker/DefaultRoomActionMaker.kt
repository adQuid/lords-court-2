package shortstate.room.actionmaker

import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.RoomAction
import shortstate.room.RoomActionMaker

class DefaultRoomActionMaker: RoomActionMaker{

    val action: RoomAction

    constructor(action: RoomAction){
        this.action = action
    }

    override fun onClick(game: ShortStateGame, perspective: ShortStateCharacter){
        action.doAction(game, perspective)
        if(action.defocusAfter()){
            UIGlobals.defocus()
        }
    }

    override fun toString(): String {
        return action.toString()
    }

}