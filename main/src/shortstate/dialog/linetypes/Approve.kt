package shortstate.dialog.linetypes

import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class Approve: Line {

    constructor(){

    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("Approve"))
    }

    override fun fullTextForm(): String {
        return "YAASSSSS"
    }

    override fun validToSend(): Boolean {
        return true
    }
}