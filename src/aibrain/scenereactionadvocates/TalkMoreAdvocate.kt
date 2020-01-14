package aibrain.scenereactionadvocates

import shortstate.Scene
import shortstate.ShortStateGame

class TalkMoreAdvocate: SceneReactionAdvocate {

    val me: game.Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(scene: Scene): Double {
        if(scene!!.conversation != null){
            if(scene!!.conversation!!.otherParticipant(scene!!.conversation!!.lastSpeaker).player.npc){
                return 8.0 - scene!!.conversation!!.age
            }
            return 0.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, scene: Scene) {
        val convo = scene.conversation!!
        if(convo.lastLine != null){
            convo.submitLine(convo.lastLine!!.AIResponseFunction(convo.otherParticipant(convo.lastSpeaker).convoBrain, convo.lastSpeaker.player, game.game), game)
        } else {
            convo.submitLine(convo.otherParticipant(convo.lastSpeaker).convoBrain.startConversation(convo.lastSpeaker.player, game.game), game)
        }
    }
}