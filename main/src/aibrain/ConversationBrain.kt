package aibrain

import shortstate.dialog.Line
import game.Game
import game.Character
import shortstate.dialog.linetypes.*

class ConversationBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun reactToLine(line: Line?, speaker: Character, game: Game): Line {
        if(line == null){
            return startConversation(line,speaker,game)
        }
        if(line is Announcement){
            return reactToAnnouncement(line,speaker,game)
        }
        if(line is Approve){
            return reactToApprove(line,speaker,game)
        }
        if(line is Disapprove){
            return reactToDisapprove(line,speaker,game)
        }
        if(line is QuestionSuggestion){
            return reactToQuestionSuggestion(line,speaker,game)
        }
        throw Error("Line class ${line.javaClass} unknown!")
    }

    private fun startConversation(line: Line?, speaker: Character, game: Game): Line {
        return SuggestAction(longBrain.sortedCases!!
            .filter{case -> case.valueToCharacter(longBrain.player) > 0}
            .filter{ case->case.plan.player == speaker}[0].plan.actions[0])
    }

    private fun reactToAnnouncement(line: Announcement, speaker: Character, game: Game): Line {
        val action = line.action

        val effectsILike = longBrain.lastFavoriteEffects!!
        val effectsOfAction = action!!.type.doAction(game, speaker)

        if(effectsILike.intersect(effectsOfAction).isNotEmpty()){
            return Approve()
        } else {
            return Disapprove()
        }
    }

    private fun reactToApprove(line: Approve, speaker: Character, game: Game): Line {
        return Approve()
    }

    private fun reactToDisapprove(line: Disapprove, speaker: Character, game: Game): Line {
        return Disapprove()
    }

    private fun reactToQuestionSuggestion(line: QuestionSuggestion, speaker: Character, game: Game): Line{
        return CiteEffect(longBrain.lastFavoriteEffects)
    }

}