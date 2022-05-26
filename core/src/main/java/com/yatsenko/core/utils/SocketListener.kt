package com.yatsenko.core.utils

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class SocketListener(
    private val onConnectedAction: () -> Unit = {},
    private val parseResponseAction: (String) -> Unit = {}
) : WebSocketListener() {

    var isConnected: Boolean = false

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        log("[webSocket] isConnected = true")
        isConnected = true
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        parseResponseAction.invoke(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        log("[webSocket] isConnected = false ${response?.message}")
        isConnected = false
        onConnectedAction.invoke()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        log("[webSocket] onClosed / isConnected = false $reason")
        isConnected = false
        onConnectedAction.invoke()
    }
}