package ui

import shortstate.ShortStateCharacter

interface Describable {

    fun tooltip(perspective: ShortStateCharacter): String

    fun description(): String

}