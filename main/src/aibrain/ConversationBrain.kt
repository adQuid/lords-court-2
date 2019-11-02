package aibrain

import shortstate.dialog.Line
import shortstate.dialog.linetypes.Announcement
import shortstate.dialog.linetypes.Approve
import shortstate.dialog.linetypes.Disapprove
import game.Game
import game.action.Action
import game.action.actionTypes.WasteTime
import game.Character
import shortstate.dialog.linetypes.Request

class ConversationBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun reactToLine(line: Line?, speaker: Character, game: Game): Line {
        //TODO: Make this not awful
        if(line == null){
            return Request(longBrain.sortedCases!![0].plan.actions[0])
        }

        if(line is Announcement){
            val action = line.action

            val effectsILike = longBrain.lastFavoriteEffects!!
            val effectsOfAction = action!!.type.doAction(game, speaker)

            if(effectsILike.intersect(effectsOfAction).isNotEmpty()){
                return Approve()
            } else {
                return Disapprove()
            }
        }
        return Announcement(Action(WasteTime()))
    }

}