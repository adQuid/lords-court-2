package scenario

import game.Game
import game.GameCharacter
import game.SpecialScript
import gamelogic.government.GovernmentLogicModule
import gamelogic.playerresources.PlayerResourceTypes

class addCharacter: SpecialScript{
    val turntoActivate: Int
    val characterToAdd: (game: Game) -> GameCharacter

    constructor(turn: Int, characterToAdd:  (game: Game) -> GameCharacter): super(turn){
        this.turntoActivate = turn
        this.characterToAdd = characterToAdd
    }

    override fun doscript(game: Game) {
        game.players.add(characterToAdd(game))
    }
}

class moveCharacter: SpecialScript{

    val character: String
    val destination: Int

    constructor(turn: Int, characterToMove: String, destinationId: Int): super(turn){
        this.character = characterToMove
        this.destination = destinationId
    }

    override fun doscript(game: Game) {
        val charToMove = game.players.filter { it.name == character }.first()
        val location = game.locationById(destination)
        charToMove.location = location
    }
}

fun tutorialSpecialScripts(): Collection<SpecialScript>{

    val makeFishMonger = { game: Game ->
    val governments = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
    val pcCapital = governments.capitals.first()
    val fishmonger = GameCharacter("Laerten", "assets/portraits/Merchant.png", true, pcCapital.location, game)
    fishmonger.resources.set(PlayerResourceTypes.FISH_NAME, 100)
    fishmonger.specialLines.add(approachTestTrigger)
    fishmonger
    }

    return listOf(
        addCharacter(2, makeFishMonger),
        moveCharacter(2, "Mayren", 7)
    )
}