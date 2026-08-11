package com.btbclient.app

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

data class BedrockState(
    val name: String = "Player",
    val dimension: String = "Unavailable",
    val x: String = "—",
    val y: String = "—",
    val z: String = "—",
    val gamemode: String = "Unavailable",
    val world: String = "Unavailable",
    val connected: Boolean = false
)

class BridgeClient(
    private val baseUrl: String = "http://127.0.0.1:8765"
) {
    private val executor = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())

    fun poll(onState: (BedrockState) -> Unit) {
        executor.execute {
            val state = try {
                val c = URL("$baseUrl/state").openConnection() as HttpURLConnection
                c.connectTimeout = 1200
                c.readTimeout = 1200
                c.requestMethod = "GET"
                val body = c.inputStream.bufferedReader().use { it.readText() }
                c.disconnect()

                val j = JSONObject(body)
                BedrockState(
                    name = j.optString("name", "Player"),
                    dimension = j.optString("dimension", "Unavailable"),
                    x = value(j, "x"),
                    y = value(j, "y"),
                    z = value(j, "z"),
                    gamemode = j.optString("gamemode", "Unavailable"),
                    world = j.optString("world", "Unavailable"),
                    connected = true
                )
            } catch (_: Exception) {
                BedrockState(connected = false)
            }
            main.post { onState(state) }
        }
    }

    private fun value(j: JSONObject, key: String): String {
        if (!j.has(key) || j.isNull(key)) return "—"
        return j.optString(key, "—")
    }
}
