package ca.tradejmark.arboreum.ktor.core

import ca.tradejmark.arboreum.Arboreum
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ArboreumCore(configuration: Configuration) {
    private val connectionString: String = configuration.connectionString
    private val databaseName: String = configuration.databaseName
    private val websocketPath: String = configuration.websocketPath ?: ""
    val arboreum by lazy {
        Arboreum(connectionString, databaseName)
    }

    private suspend fun respond(msg: Message): String = when (msg) {
        is Message.GetBranchMessage -> Json.encodeToString(arboreum.getBranch(msg.branchSlug))
        is Message.GetLeafMessage -> Json.encodeToString(arboreum.getLeaf(msg.branchSlug, msg.leafSlug))
    }

    class Configuration {
        lateinit var connectionString: String
        lateinit var databaseName: String
        var websocketPath: String? = null
    }

    companion object Feature: ApplicationFeature<Application, Configuration, ArboreumCore> {
        override val key = AttributeKey<ArboreumCore>("ArboreumCore")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): ArboreumCore {
            val conf = Configuration().apply(configure)
            val core = ArboreumCore(conf)
            pipeline.featureOrNull(WebSockets) ?: pipeline.install(WebSockets)
            pipeline.feature(Routing).webSocket(core.websocketPath) {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> send(core.respond(MessageParser.parse(frame.readText())))
                        is Frame.Close -> close()
                        else -> throw ArboreumParseException("Cannot parse message of type ${frame.frameType}")
                    }
                }
            }
            return core
        }
    }
}