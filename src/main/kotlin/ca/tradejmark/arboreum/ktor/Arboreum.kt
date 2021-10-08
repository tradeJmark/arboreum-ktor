package ca.tradejmark.arboreum.ktor

import ca.tradejmark.arboreum.ktor.core.ArboreumCore
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.util.*

class Arboreum {
    class Configuration {
        internal var coreOptions: (ArboreumCore.Configuration.() -> Unit)? = null
        fun connection(connConf: ArboreumCore.Configuration.() -> Unit) {
            coreOptions = connConf
        }
    }
    companion object Feature: ApplicationFeature<Application, Configuration, Arboreum> {
        override val key = AttributeKey<Arboreum>("Arboreum")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): Arboreum {
            val conf = Configuration().apply(configure)
            conf.coreOptions?.let {
                pipeline.install(ArboreumCore, it)
            }
            pipeline.featureOrNull(ArboreumCore) ?: throw UnconfiguredConnectionException()
            return Arboreum()
        }

        fun Route.arboreum(
            rootBranch: String,
            block: ArboreumContext.() -> Unit) {
            val arb = application.feature(ArboreumCore).arboreum
            val ctx = ArboreumContext().apply(block)
            get("{structure...}") {
                var branch = arb.getBranch(rootBranch)
                val structure = call.parameters["structure"]!!.split("/")
                structure.drop(1).forEach { seg ->
                    if (branch.branches.any { it.slug == seg }) branch = arb.getBranch(seg)
                    else throw NotFoundException("Branch ${branch.meta.slug} has no child $seg.")
                }
                val finalSlug = structure.last()
                val branchSlug = branch.branches.firstOrNull { it.slug == finalSlug }?.slug
                if (branchSlug != null) {
                    val bCtx = BranchContext(branchSlug, this)
                    ctx.onBranch(bCtx)
                }
                else {
                    val leafSlug = branch.leaves.firstOrNull { it.slug == finalSlug }?.slug
                        ?: throw NotFoundException("Branch ${branch.meta.slug} has no child $finalSlug")
                    val lCtx = LeafContext(leafSlug, branch.meta.slug, this)
                    ctx.onLeaf(lCtx)
                }
            }
        }
    }
}