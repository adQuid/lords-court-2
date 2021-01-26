package scenario.tutorial

import game.Game
import gamelogic.government.Count
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.SimpleLine
import shortstate.dialog.linetypes.TreeLine
import shortstate.linetriggers.*

object KairethAdvice {

    fun initialKairethAdvice(): LineTrigger {
        return LineTrigger(
            "initialKaireth",
            and(neverBeenCalled, safeForNewTopic, speakerIsPlayer, speakerIsCount),
            replyWithTreeLine(TreeLine("I've been granted Port Fog. What do I do now?", SimpleLine("You rule, my prince. With no pressing matters, I would advise proceeding to the throne room and listening to petitioners. Don't do anything before consulting me, of course.")))
        )
    }

    fun initialKairethAdvicePreTitle(): LineTrigger {
        return LineTrigger(
            "initialKairethPretitile",
            and(neverBeenCalled, safeForNewTopic, speakerIsPlayer, talkingToSpecific("Kaireth"), otherTriggerCalledByCharacter("talktodad4", "Mayren")),
            replyWithTreeLine(TreeLine("My father is going to grant me Port Fog. What do I do now?", SimpleLine("For now, nothing. Pray, get some rest. It will be some time before your title is known, and your duty as a ruler begins. Until then, enjoy your last nights as a boy. Then come back and talk to me before you do anything foolish with power.")))
        )
    }

    val speakerIsCount = { data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter? ->
        me.player.titles.filter { it is Count }.isNotEmpty()}
}