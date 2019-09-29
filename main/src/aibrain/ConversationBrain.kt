package aibrain

import dialog.Line
import dialog.linetypes.Announcement
import game.Player

class ConversationBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun reactToLine(line: Line, speaker: Player): Line {
        return Announcement(null)
    }

}