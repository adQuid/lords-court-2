package shortstate.dialog

import shortstate.ShortStateCharacter

object DialogFormatter {

    fun gainOrLose(value: Double): String{
        if(value > 0){
            return "gain ${value}"
        } else {
            return "lose ${-value}"
        }
    }

    fun applyPronouns(text: String, speaker: ShortStateCharacter, target: ShortStateCharacter): String{
        return text.replace(speaker.player.name, "me").replace(target.player.name, "you")
    }

    fun applyPunctuationAndCapitalization(text: String): String{
        var retval = text
        val punctuations = ".?!"
        if(!punctuations.contains(retval.takeLast(1))){
            retval = retval + "."
        }
        return retval
    }

}