package ca.tradejmark.arboreum.ktor

import io.ktor.application.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

class LeafContext internal constructor(
    val slug: String,
    val branchSlug: String,
    parentContext: PipelineContext<Unit, ApplicationCall>
): PipelineContext<Unit, ApplicationCall> by parentContext {
    fun FlowContent.leafTitle(customClasses: String? = null, block: DIV.() -> Unit = {}) {
        div(customClasses) {
            block()
            classes += "arboreum-leaf-title"
        }
    }

    fun FlowContent.leafDescription(customClasses: String? = null, block: DIV.() -> Unit = {}) {
        div(customClasses) {
            block()
            classes += "arboreum-leaf-desc"
        }
    }

    fun FlowContent.leafBody(customClasses: String? = null, block: DIV.() -> Unit = {}) {
        div(customClasses) {
            block()
            attributes["data-arboreum-branch-slug"] = branchSlug
            attributes["data-arboreum-leaf-slug"] = slug
            classes += "arboreum-leaf-body"
        }
    }
}