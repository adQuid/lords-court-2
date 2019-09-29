package aibrain

import dialog.Line
import dialog.linetypes.Announcement
import dialog.linetypes.Approve
import dialog.linetypes.Disapprove
import game.Game
import game.Player

class ConversationBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun reactToLine(line: Line, speaker: Player, game: Game): Line {
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
        return Announcement(null)
    }

}