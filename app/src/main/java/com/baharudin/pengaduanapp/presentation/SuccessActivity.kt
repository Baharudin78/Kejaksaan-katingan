package com.baharudin.pengaduanapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baharudin.pengaduanapp.R
import com.baharudin.pengaduanapp.databinding.ActivitySuccessBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuccessActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}