package aibrain

import main.Controller

class BrainThread: Runnable {

    private val parent: Controller

    constructor(parent: Controller){
        this.parent = parent
    }

    override fun run() {
        while(true){
            val player = parent.game?.nextPlayerToForcast()

            if(player != null){
                parent.game!!.commitActionsForPlayer(player, player.brain.thinkAboutNextTurn(parent.game!!))
                println("turn commited for $player")

            } else {
                val shortPlayer = parent.shortGame?.nextPlayerToDoShortStateStuff()

                if(shortPlayer == null){
                    Thread.sleep(200)
                    continue
                }

                val scene = parent.shortGame!!.sceneForPlayer(shortPlayer)

                //TODO: Make this do something relivent
                if(scene == null){
                    Thread.sleep(200)
                    continue
                }

                if(scene!!.characters.size == 1){
                    parent.shortGame!!.endScene(scene)
                }
            }


        }
    }
}