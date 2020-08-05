package game.linetriggers

import shortstate.dialog.Line

class LineTrigger {

    val condition: () -> Line?

    constructor(condition: () -> Line?){
        this.condition = condition
    }

}