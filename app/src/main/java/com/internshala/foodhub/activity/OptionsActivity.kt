package com.internshala.foodhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.internshala.foodhub.R

class OptionsActivity : AppCompatActivity() {

    private lateinit var btnUser: Button
    private lateinit var btnAdmin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        btnUser = findViewById(R.id.btnUser)
        btnAdmin = findViewById(R.id.btnAdmin)

        btnUser.setOnClickListener() {
            val userIntent = Intent(this@OptionsActivity, LoginActivity::class.java)
            startActivity(userIntent)
        }

        btnAdmin.setOnClickListener() {
            val adminIntent = Intent(this@OptionsActivity, AdminLoginActivity::class.java)
            startActivity(adminIntent)
        }
    }
}


