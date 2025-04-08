package com.example.androidadvlab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidadvlab2.databinding.ActivityMainBinding
import kz.diasjakupov.websocket_chat.ChatBuilder
import kz.diasjakupov.websocket_chat.databinding.ChatActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartChat.setOnClickListener {
            ChatBuilder.start(this)
        }
    }
}