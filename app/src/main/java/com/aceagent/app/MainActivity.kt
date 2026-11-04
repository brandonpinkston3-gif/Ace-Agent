
package com.aceagent.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private lateinit var chat: TextView
    private lateinit var message: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(18, 20, 30))
        }

        val title = TextView(this).apply {
            text = "ACE AGENT"
            textSize = 28f
            setTextColor(Color.CYAN)
            gravity = Gravity.CENTER
        }

        chat = TextView(this).apply {
            text = "Ace: Hello! I'm your personal AI assistant.\n\nWhat would you like me to do?"
            textSize = 18f
            setTextColor(Color.WHITE)
            setPadding(8, 24, 8, 24)
        }

        val scroll = ScrollView(this)
        scroll.addView(chat)

        message = EditText(this).apply {
            hint = "Tell Ace what to do..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        val send = Button(this).apply {
            text = "SEND"
            setOnClickListener {
                val input = message.text.toString().trim()

                if (input.isNotEmpty()) {
                    chat.append("\n\nYou: $input")
                    chat.append("\n\nAce: I received your command. My action engine is coming next!")
                    message.text.clear()
                    scroll.post {
                        scroll.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            }
        }

        layout.addView(title)
        layout.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )
        layout.addView(message)
        layout.addView(send)

        setContentView(layout)
    }
}
