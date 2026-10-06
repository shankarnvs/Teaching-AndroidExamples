package com.cutm.demo.websocketsapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var webSocket: WebSocket
    private lateinit var priceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */
        priceTextView = TextView(this).apply {
            text = "BTC/USDT\nWaiting for price..."
            textSize = 28f
            setPadding(30, 50, 30, 50)
        }

        setContentView(priceTextView)
        connectToBinance()
    }


    private fun connectToBinance() {

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("wss://stream.binance.com:9443/ws/btcusdt@trade")
            .build()

        Log.d("BINANCE_WS", "Connecting to Binance...")

        webSocket = client.newWebSocket(
            request,
            object : WebSocketListener() {

                override fun onOpen(
                    webSocket: WebSocket,
                    response: okhttp3.Response
                ) {
                    Log.d(
                        "BINANCE_WS",
                        "WebSocket connection opened"
                    )
                }

                override fun onMessage(
                    webSocket: WebSocket,
                    text: String
                ) {
                    val jsonObject = JSONObject(text)

                    val trade = TradeData(
                        symbol = jsonObject.getString("s"),
                        price = jsonObject.getString("p").toDouble(),
                        quantity = jsonObject.getString("q").toDouble(),
                        tradeId = jsonObject.getLong("t")
                    )
                    Log.d(
                        "BINANCE_WS",
                        "Trade: $trade"
                    )

                    runOnUiThread {
                        priceTextView.text =
                            "${trade.symbol}\n${trade.price}"
                    }
                }

                override fun onClosing(
                    webSocket: WebSocket,
                    code: Int,
                    reason: String
                ) {
                    Log.d(
                        "BINANCE_WS",
                        "WebSocket closing: $code - $reason"
                    )
                }

                override fun onClosed(
                    webSocket: WebSocket,
                    code: Int,
                    reason: String
                ) {
                    Log.d(
                        "BINANCE_WS",
                        "WebSocket closed: $code - $reason"
                    )
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {
                    Log.e(
                        "BINANCE_WS",
                        "WebSocket error: ${t.message}",
                        t
                    )
                }
            }
        )
    }
}