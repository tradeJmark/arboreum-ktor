package ca.tradejmark.arboreum.ktor.core

internal sealed interface Message {
    data class GetBranchMessage(val branchSlug: String): Message
    data class GetLeafMessage(val branchSlug: String, val leafSlug: String): Message
}
