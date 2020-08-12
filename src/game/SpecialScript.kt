package game

import scenario.tutorialSpecialScripts

fun specialScriptsFromId(id: String): Collection<SpecialScript>{
    if(id == "tutorial"){
        return tutorialSpecialScripts()
    }

    return listOf()
}

abstract class SpecialScript {

    constructor(turn: Int){
        this.turnToActivate = turn
    }

    val turnToActivate: Int

    abstract fun doscript(game: Game)

}