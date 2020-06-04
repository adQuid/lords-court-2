package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.SceneReactionAdvocate

class SceneCreationWeight {

    val weight: Double
    val roomAction: SceneReactionAdvocate

    constructor(weight: Double, roomAction: SceneReactionAdvocate) {
        this.weight = weight
        this.roomAction = roomAction
    }
}