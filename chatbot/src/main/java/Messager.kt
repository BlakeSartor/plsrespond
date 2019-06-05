package chatbot

import io.javalin.websocket.WsSession
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import j2html.TagCreator.*


class Messager {
    fun broadcastMessage(sender: String, message: String, userNameMap : ConcurrentHashMap<WsSession, String>) {
        userNameMap.keys.filter { it.isOpen }.forEach { session ->
            session.send(
                    JSONObject()
                            .put("userMessage", createHtmlMessageFromSender(sender, message))
                            .put("userlist", userNameMap.values).toString()
            )
        }
    }

    private fun createHtmlMessageFromSender(sender: String, message: String): String {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), SimpleDateFormat("HH:mm:ss").format(Date())),
                p(message)
        ).render()
    }
}