package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityMyServicesBinding

class MyServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyServicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}