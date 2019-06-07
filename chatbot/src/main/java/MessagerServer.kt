package chatbot

import io.javalin.Javalin
import io.javalin.websocket.WsSession
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private val userNameMap = ConcurrentHashMap<WsSession, String >()

private var nextUserNumber = 1

fun main(args: Array<String>) {

    val messager = Messager()

        Javalin.create().apply {
        enableStaticFiles("/public")
        ws("/chat") { ws ->
            ws.onConnect { session ->
                val username = "User" + nextUserNumber++
                userNameMap.put(session, username)
                messager.broadcastMessage("Server", username + " joined the chat", userNameMap)
            }
            ws.onClose { session, status, message ->
                val username = userNameMap[session]
                userNameMap.remove(session)
                messager.broadcastMessage("Server", username + " left the chat", userNameMap)
            }
            ws.onMessage { session, message ->
                messager.broadcastMessage(userNameMap[session]!!, message, userNameMap)
            }
        }
    }.start(7070)
}
