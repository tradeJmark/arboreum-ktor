package ca.tradejmark.arboreum.ktor

import io.ktor.application.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

class BranchContext internal constructor(
    val slug: String,
    parentContext: PipelineContext<Unit, ApplicationCall>
): PipelineContext<Unit, ApplicationCall> by parentContext {
    fun FlowContent.branchName(customClasses: String?, block: DIV.() -> Unit = {}) {
        div(customClasses) {
            block()
            classes += "arboreum-branch-name"
            attributes["data-arboreum-branch-slug"] = slug
        }
    }
    fun FlowContent.branchDescription(customClasses: String?, block: DIV.() -> Unit = {}) {
        div(customClasses) {
            block()
            classes += "arboreum-branch-desc"
        }
    }
    fun FlowContent.childLeaf(index: Int, customClasses: String?, block: ChildLeaf.() -> Unit) {
        ChildLeaf(index, customClasses, consumer).visit(block)
    }
    fun FlowContent.childBranch(index: Int, customClasses: String?, block: ChildBranch.() -> Unit) {
        ChildBranch(index, customClasses, consumer).visit(block)
    }
    fun FlowContent.allChildLeaves(containerClasses: String?, childClasses: String?, block: ChildLeaf.() -> Unit) {
        div(containerClasses) {
            classes += "arboreum-child-leaves"
            ChildLeaf(0, childClasses, consumer).visit(block)
        }
    }
    fun FlowContent.allChildBranches(containerClasses: String?, childClasses: String?, block: ChildBranch.() -> Unit) {
        div(containerClasses) {
            classes += "arboreum-child-branches"
            ChildBranch(0, childClasses, consumer).visit(block)
        }
    }

    sealed class Child(
        private val type: String,
        index: Int,
        customClasses: String?,
        parent: TagConsumer<*>
    ): DIV(mapOf("data-arboreum-child-$type-index" to index.toString()), parent) {
        init {
            customClasses?.split(",")?.toSet()?.let { classes = it }
            classes += "arboreum-child-$type"
        }
        fun FlowContent.childTitle(customClasses: String?, block: DIV.() -> Unit = {}) {
            div(customClasses) {
                block()
                classes += "arboreum-child-${this@Child.type}-title"
            }
        }
        fun FlowContent.childDescription(customClasses: String?, block: DIV.() -> Unit = {}) {
            div(customClasses) {
                block()
                classes += "arboreum-child-${this@Child.type}-description"
            }
        }
        fun FlowContent.childLink(customClasses: String?, block: A.() -> Unit = {}) {
            a(classes = customClasses) {
                block()
                classes += "arboreum-child-${this@Child.type}-link"
            }
        }
    }

    class ChildBranch(
        index: Int,
        customClasses: String?,
        parent: TagConsumer<*>
    ): Child("branch", index, customClasses, parent)
    class ChildLeaf(
        index: Int,
        customClasses: String?,
        parent: TagConsumer<*>
    ): Child("leaf", index, customClasses, parent)
}