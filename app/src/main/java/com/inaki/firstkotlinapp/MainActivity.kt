package com.inaki.firstkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inaki.firstkotlinapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        binding.button.setOnClickListener {
            Intent(baseContext, MainActivity2::class.java)
                .apply {
                    putExtra(MainActivity2.DATA, binding.editText.text.toString())
                    startActivity(this)
                }
        }
    }
}