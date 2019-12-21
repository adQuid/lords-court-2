package shortstate.dialog

abstract class Line {

    abstract fun tooltipName(): String

    abstract fun symbolicForm(): List<LineBlock>

    abstract fun fullTextForm(): String

    abstract fun validToSend(): Boolean

    abstract fun possibleReplies(): List<Line>

 }