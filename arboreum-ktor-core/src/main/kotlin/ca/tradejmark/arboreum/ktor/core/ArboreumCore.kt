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
    private val arb by lazy {
        Arboreum(connectionString, databaseName)
    }

    private suspend fun respond(msg: Message): String = when (msg) {
        is Message.GetBranchMessage -> Json.encodeToString(arb.getBranch(msg.branchSlug))
        is Message.GetLeafMessage -> Json.encodeToString(arb.getLeaf(msg.branchSlug, msg.leafSlug))
    }

    class Configuration {
        lateinit var connectionString: String
        lateinit var databaseName: String
    }

    companion object Feature: ApplicationFeature<Application, Configuration, ArboreumCore> {
        override val key = AttributeKey<ArboreumCore>("ca.tradejmark.arboreum.ktor.core.ArboreumCore")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): ArboreumCore {
            pipeline.featureOrNull(WebSockets) ?: pipeline.install(WebSockets)

            val conf = Configuration().apply(configure)
            return ArboreumCore(conf)
        }

        fun Route.arboreumCore(action: DefaultWebSocketServerSession.() -> Unit) {
            val core = application.feature(ArboreumCore)
            webSocket {
                action()
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> send(core.respond(MessageParser.parse(frame.readText())))
                        is Frame.Close -> close()
                        else -> throw ArboreumParseException("Cannot parse message of type ${frame.frameType}")
                    }
                }
            }
        }
    }
}