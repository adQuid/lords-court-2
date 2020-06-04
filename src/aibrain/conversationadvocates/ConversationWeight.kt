package aibrain.conversationadvocates

import shortstate.dialog.Line

class ConversationWeight {

    val weight: Double
    val line: Line

    constructor(weight: Double, line: Line) {
        this.weight = weight
        this.line = line
    }
}