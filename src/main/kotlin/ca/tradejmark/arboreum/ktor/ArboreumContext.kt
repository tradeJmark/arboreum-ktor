package ca.tradejmark.arboreum.ktor

class ArboreumContext internal constructor() {
    internal lateinit var onBranch: BranchContext.() -> Unit
    fun branch(setup: BranchContext.() -> Unit) {
        onBranch = setup
    }

    internal lateinit var  onLeaf: LeafContext.() -> Unit
    fun leaf(setup: LeafContext.() -> Unit) {
        onLeaf = setup
    }
}