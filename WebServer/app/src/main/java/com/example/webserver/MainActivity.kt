package com.example.webserver

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.URI
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClient
    private lateinit var textView: TextView
    private lateinit var textView2: TextView

    private lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView3)

        editText = findViewById(R.id.editTextText)
        val sendMessageButton: Button = findViewById(R.id.button)
        connectWebSocket()
        SocketServerTask().execute()
        sendMessageButton.setOnClickListener {
            // Send a message to the server
            val text = editText.text.toString()
            webSocketClient.send(text)
            editText.setText(" ")
        }

    }

    private inner class SocketServerTask : AsyncTask<Void, Void, String>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg voids: Void): String? {
            return try {
                val serverSocket = ServerSocket(8888)
                Log.w(
                    "SocketServerTask",
                    "ServerSocket created. Waiting for incoming connection..."
                )

                val socket = serverSocket.accept()
                Log.w("SocketServerTask", "Connection established.")

                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val message = input.readLine()
                Log.w("SocketServerTask", "Received message: $message")

                serverSocket.close()
                message
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        @Deprecated(
            "Deprecated in Java", ReplaceWith(
                "Log.d(\"SocketServerTask\", \"Message received: \$message\")",
                "android.util.Log"
            )
        )
        override fun onPostExecute(message: String?) {
            // Process the received message, e.g., update UI
            Log.d("SocketServerTask", "Message received: $message")
            textView2.text = message
        }
    }

    private fun connectWebSocket() {
        val uri: URI
        try {
            uri = URI("ws://192.168.43.212:8080")  //pc ip address
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.w("WebSocket", "URI Syntax Exception: ${e.message}")
            return
        }

        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake) {
                Log.w("WebSocket", "Connected to server")
            }

            override fun onMessage(message: String) {
                runOnUiThread {
                    textView.text = message
                }
            }

            override fun onClose(code: Int, reason: String, remote: Boolean) {
                Log.w("WebSocket", "Connection closed: $reason")
            }

            override fun onError(ex: Exception) {
                Log.w("WebSocket", "Error: ${ex.message}")
            }
        }
        webSocketClient.connect()
    }
}