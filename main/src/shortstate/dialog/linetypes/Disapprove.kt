package shortstate.dialog.linetypes

import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class Disapprove: Line {

    constructor(){

    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("Disapprove"))
    }

    override fun fullTextForm(): String {
        return "NOOO!!!"
    }

    override fun validToSend(): Boolean {
        return true
    }
}