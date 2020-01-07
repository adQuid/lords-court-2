package shortstate.dialog.linetypes

import shortstate.dialog.Line

object GlobalLineTypeList {
    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Line> = hashMapOf(
        "Approve" to {map -> Approve() }
    )

    fun fromMap(map: Map<String, Any>): Line {
        return typeMap[map[TYPE_NAME]]!!(map)
    }
}