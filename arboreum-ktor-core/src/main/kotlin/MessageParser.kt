internal object MessageParser {
    fun parse(message: String): Message {
        val (action, payload) = message.split(" ").headPartition()
        return when (action) {
            "get" -> parseGet(payload)
            else -> throw ArboreumParseException("Action type '$action' not supported.")
        }
    }

    private fun parseGet(payload: List<String>): Message {
        val (type, data) = payload.headPartition()
        return when(type) {
            "branch" -> Message.GetBranchMessage(data[0])
            "leaf" -> Message.GetLeafMessage(data[0], data[1])
            else -> throw ArboreumParseException("Unable to get content of type '$type'.")
        }
    }
}