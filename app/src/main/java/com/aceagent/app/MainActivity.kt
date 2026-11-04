package com.aceagent.app

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import android.content.pm.PackageManager

class MainActivity : Activity() {

    private lateinit var chat: TextView
    private lateinit var message: EditText
    private lateinit var scroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.rgb(15, 18, 30))
        }

        val title = TextView(this).apply {
            text = "ACE AGENT"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(Color.CYAN)
            setPadding(10, 20, 10, 20)
        }

        chat = TextView(this).apply {
            text = "Ace: Hello! I'm ready. Tell me what you want me to do."
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(10, 15, 10, 15)
        }

        scroll = ScrollView(this)
scroll.addView(
    chat,
    ScrollView.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
)

        message = EditText(this).apply {
            hint = "Tell Ace what to do..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setSingleLine(true)
        }

        val send = Button(this).apply {
    text = "SEND"

    setOnClickListener {
        val command = message.text.toString().trim()

        if (command.isNotEmpty()) {
            chat.append("\n\nYou: $command")
            message.text.clear()

            runCommand(command)

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
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        layout.addView(message)
        layout.addView(send)

        setContentView(layout)
    }

    private fun reply(text: String) {
        chat.append("\n\nAce: $text")
        scroll.post {
            scroll.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun runCommand(command: String) {
        val input = command.lowercase()

        try {
            when {
                input.contains("open settings") -> {
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                    reply("Opening Settings.")
                }

                input.contains("open camera") -> {
                    startActivity(Intent("android.media.action.IMAGE_CAPTURE"))
                    reply("Opening Camera.")
                }

                input.contains("open browser") ||
                input.contains("open internet") ||
                input.contains("open chrome") -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://www.google.com")
                    )
                    startActivity(intent)
                    reply("Opening your browser.")
                }

                input == "home" -> {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    startActivity(intent)
                    reply("Going to your home screen.")
                }

                input.contains("help") -> {
                    reply(
                        "Try these commands:\n" +
                        "Open Settings\n" +
                        "Open Camera\n" +
                        "Open Browser\n" +
                        "Home\n" +
                        "Open YouTube"
                    )
                }

                input.startsWith("open ") -> {
                    val appName = input.removePrefix("open ").trim()
                    openInstalledApp(appName)
                }

                else -> {
                    reply(
                        "I received your command. " +
                        "My AI connection and advanced action engine " +
                        "still need to be added."
                    )
                }
            }
        } catch (e: Exception) {
            reply("I couldn't complete that action: ${e.message}")
        }
    }

    private fun openInstalledApp(appName: String) {
        val launcherIntent = Intent(Intent.ACTION_MAIN)
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val apps = packageManager.queryIntentActivities(
            launcherIntent,
            PackageManager.MATCH_ALL
        )

        val match = apps.firstOrNull {
            it.loadLabel(packageManager)
                .toString()
                .contains(appName, ignoreCase = true)
        }

        if (match != null) {
            val packageName = match.activityInfo.packageName
            val intent = packageManager.getLaunchIntentForPackage(packageName)

            if (intent != null) {
                startActivity(intent)
                reply("Opening ${match.loadLabel(packageManager)}.")
            } else {
                reply("I couldn't launch that app.")
            }
        } else {
            reply("I couldn't find an installed app named $appName.")
        }
    }
}
