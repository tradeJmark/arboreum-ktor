package ca.tradejmark.arboreum.ktor.core.common

import kotlinx.serialization.Serializable

sealed interface Message {
    @Serializable
    data class GetBranchMessage(val branchSlug: String): Message
    @Serializable
    data class GetLeafMessage(val branchSlug: String, val leafSlug: String): Message
}
