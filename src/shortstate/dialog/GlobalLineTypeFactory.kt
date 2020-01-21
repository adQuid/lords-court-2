package shortstate.dialog

import game.Game
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.*

object GlobalLineTypeFactory {

    val ACCEPT_DEAL_TYPE_NAME = "AcptDeal"
    val ANNOUNCEMENT_TYPE_NAME = "Ancmnt"
    val APPROVE_TYPE_NAME = "Aprve"
    val CITE_EFFECT_TYPE_NAME = "CiteEfct"
    val DISAPPROVE_TYPE_NAME = "Disaprve"
    val GIVE_REPORT_TYPE_NAME = "GveRprt"
    val OFFER_DEAL_TYPE_NAME = "OfrDeal"
    val QUESTION_SUGGESTION_TYPE_NAME = "QstnSug"
    val REJECT_DEAL_TYPE_NAME = "RjctDeal"
    val REQUEST_REPORT_TYPE_NAME = "RqstRprt"
    val SUGGEST_ACTION_TYPE_NAME = "SgstActn"

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Line> = hashMapOf(
        ACCEPT_DEAL_TYPE_NAME to {map, game -> AcceptDeal(map, game)},
        ANNOUNCEMENT_TYPE_NAME to {map, game -> Announcement(map, game)},
        APPROVE_TYPE_NAME to {map, game -> Approve() },
        CITE_EFFECT_TYPE_NAME to {map, game -> CiteEffect(map, game) },
        DISAPPROVE_TYPE_NAME to {map, game -> Disapprove() },
        GIVE_REPORT_TYPE_NAME to {map, game -> GiveReport(map)},
        OFFER_DEAL_TYPE_NAME to {map, game -> OfferDeal(map, game)},
        QUESTION_SUGGESTION_TYPE_NAME to {map, game -> QuestionSuggestion(map, game)},
        REJECT_DEAL_TYPE_NAME to {map, game -> RejectDeal(map, game)},
        REQUEST_REPORT_TYPE_NAME to {map, game -> RequestReport(map)},
        SUGGEST_ACTION_TYPE_NAME to {map, game -> SuggestAction(map, game)}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Line {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }
}