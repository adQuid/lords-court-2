package ui.commoncomponents

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

interface PrettyPrintable {

    fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String
}