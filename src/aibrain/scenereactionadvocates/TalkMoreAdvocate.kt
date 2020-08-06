package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell
import shortstate.room.RoomAction
import shortstate.room.action.SayLine

class TalkMoreAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        if(shortGameScene!!.conversation != null){
            if(shortGameScene!!.conversation!!.otherParticipant(shortGameScene!!.conversation!!.lastSpeaker).player.npc){
                if(IAmStartingThisConvo(shortGameScene)){
                    return shortGameScene!!.conversation!!.otherParticipant(shortGameScene!!.conversation!!.lastSpeaker)
                        .convoBrain.bestWeight(shortGameScene!!.conversation!!.lastSpeaker.player, game.game)
                }
                if(!(nextLineIWouldSay(game, shortGameScene) is Farewell)){
                    return 50.0 - shortGameScene.conversation!!.age //TODO: This needs to make more sense
                }
            }
            return 0.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        return SayLine(nextLineIWouldSay(game, shortGameScene))
    }

    private fun IAmStartingThisConvo(shortGameScene: ShortGameScene): Boolean{
        val convo = shortGameScene.conversation!!
        return convo.lastLine == null
    }

    private fun nextLineIWouldSay(game: ShortStateGame, shortGameScene: ShortGameScene): Line {
        val convo = shortGameScene.conversation!!
        val specialLine = me.specialLines.filter { it.shouldGenerateLine(game.game, convo.lastLine, game.shortPlayerForLongPlayer(me)!!)}.firstOrNull()
        if(specialLine != null){
            return specialLine.generateLine(game.game, convo.lastLine, game.shortPlayerForLongPlayer(me)!!)
        }
        if(convo.lastLine != null){
            return convo.lastLine!!.AIResponseFunction(convo.otherParticipant(convo.lastSpeaker).convoBrain, convo.lastSpeaker.player, game.game)
        } else {
            return convo.otherParticipant(convo.lastSpeaker).convoBrain.startConversation(convo.lastSpeaker.player, game.game)
        }
    }
}