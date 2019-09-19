package aibrain

import main.Controller

class BrainThread: Runnable {

    private val parent: Controller

    constructor(parent: Controller){
        this.parent = parent
    }

    override fun run() {
        while(true){
            val curPlayer = parent.game?.nextPlayerWithoutActions()

            if(curPlayer == null){
                Thread.sleep(200)
                continue
            }

            curPlayer.brain.lastIdea = curPlayer.brain.casesOfConcern(parent.game!!)
        }
    }
}