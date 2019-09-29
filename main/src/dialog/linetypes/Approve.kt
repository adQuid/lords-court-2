package dialog.linetypes

import dialog.Line
import dialog.LineBlock

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