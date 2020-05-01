package ui.commoncomponents

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

interface PrettyPrintable {

    abstract fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String
}