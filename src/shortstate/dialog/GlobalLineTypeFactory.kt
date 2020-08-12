package shortstate.dialog

import game.Game
import shortstate.dialog.linetypes.*

object GlobalLineTypeFactory {

    val ACCEPT_DEAL_TYPE_NAME = "AcptDeal"
    val ANNOUNCEMENT_TYPE_NAME = "Ancmnt"
    val APPROVE_TYPE_NAME = "Aprve"
    val DISAPPROVE_TYPE_NAME = "Disaprve"
    val GIVE_REPORT_TYPE_NAME = "GveRprt"
    val OFFER_DEAL_TYPE_NAME = "OfrDeal"
    val REQUEST_DEAL_ADVICE_TYPE_NAME = "RqstAdvDeal"
    val QUESTION_OFFER_TYPE_NAME = "QstnOfr"
    val REJECT_DEAL_TYPE_NAME = "RjctDeal"
    val REQUEST_REPORT_TYPE_NAME = "RqstRprt"
    val SIMPLE_TYPE_NAME = "Smpl"
    val TREE_TYPE_NAME = "Tree"
    val OFFER_WRIT_TYPE_NAME = "OfrWrt"
    val FAREWELL_TYPE_NAME = "Farewell"
    val ABANDON_TYPE_NAME = "Abandn"

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Line> = hashMapOf(
        ACCEPT_DEAL_TYPE_NAME to {map, game -> AcceptDeal(map, game)},
        ANNOUNCEMENT_TYPE_NAME to {map, game -> Announcement(map, game)},
        APPROVE_TYPE_NAME to {map, game -> Approve() },
        DISAPPROVE_TYPE_NAME to {map, game -> Disapprove() },
        GIVE_REPORT_TYPE_NAME to {map, game -> GiveReport(map, game)},
        OFFER_DEAL_TYPE_NAME to {map, game -> OfferDeal(map, game)},
        QUESTION_OFFER_TYPE_NAME to { map, game -> QuestionOffer(map, game)},
        REJECT_DEAL_TYPE_NAME to {map, game -> RejectDeal(map, game)},
        REQUEST_REPORT_TYPE_NAME to {map, game -> RequestReport(map)},
        REQUEST_DEAL_ADVICE_TYPE_NAME to {map, game -> RequestAdviceForDeal(map,game)},
        SIMPLE_TYPE_NAME to {map, game -> SimpleLine(map, game)},
        TREE_TYPE_NAME to {map, game -> TreeLine(map, game)},
        OFFER_WRIT_TYPE_NAME to {map, game -> OfferWrit(map, game)},
        FAREWELL_TYPE_NAME to {map, game -> Farewell()},
        ABANDON_TYPE_NAME to {map, game -> AbandonConversation()}
    )

    fun fromMap(map: Map<String, Any>, game: Game): Line {
        return typeMap[map[TYPE_NAME]]!!(map, game)
    }
}