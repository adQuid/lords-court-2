package aibrain

import shortstate.dialog.Line
import game.Game
import game.Character
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.*

class ConversationBrain {

    val shortCharacter: ShortStateCharacter

    constructor(shortCharacter: ShortStateCharacter){
        this.shortCharacter = shortCharacter
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
        if(line is RequestReport){
            return reactToRequestReport(line, speaker, game)
        }
        throw Error("Line class ${line.javaClass} unknown!")
    }

    private fun startConversation(line: Line?, speaker: Character, game: Game): Line {
        val thingToSuggest = shortCharacter.player.brain.lastCasesOfConcern!!
            .filter{case -> case.valueToCharacter(shortCharacter.player.brain.player) > 0}
            .filter{ case->case.plan.player == speaker}

        if(thingToSuggest.isNotEmpty()){
            return SuggestAction(thingToSuggest[0].plan.actions[0])
        }
        return Approve()
    }

    private fun reactToAnnouncement(line: Announcement, speaker: Character, game: Game): Line {
        val action = line.action

        val effectsILike = shortCharacter.player.brain.lastFavoriteEffects!!
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
        return CiteEffect(shortCharacter.player.brain.lastFavoriteEffects)
    }

    private fun reactToRequestReport(line: RequestReport, speaker: Character, game: Game): Line{
        if(shortCharacter.knownReports.filter { report -> report.type() == line.myGetReportType() }.isNotEmpty()){
            return GiveReport(shortCharacter.knownReports.filter { report -> report.type() == line.myGetReportType() }[0])
        } else {
            return Approve()
        }
    }
}