package com.example.ktorchat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ktorchat.presentation.chat.ChatViewModel
import com.example.ktorchat.presentation.username.UsernameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val chatViewModel by viewModels<ChatViewModel>()
    private val usernameViewModel by viewModels<UsernameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.joinChatButton).setOnClickListener {
            chatViewModel.sendMessage()
        }
        findViewById<EditText>(R.id.usernameEditText).doOnTextChanged { text, start, before, count ->
            chatViewModel.onMessageChange(text.toString())
        }

        lifecycleScope.launch {
            chatViewModel.toastEvent.collect {
                Log.d("xxx ", "toastEvent $it")
            }
            chatViewModel.messageText.collect {
                Log.d("xxx ", "messageText $it")
            }
            chatViewModel.state.collectLatest {
                findViewById<TextView>(R.id.detailTextView).text = it.toString()
                Log.d("xxx ", "state $it")
            }
        }
    }
}