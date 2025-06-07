package com.putradwicahyono.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class login : AppCompatActivity() {

    lateinit var etusn: EditText
    lateinit var etpass: EditText
    lateinit var btlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        init()
        login()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        etusn = findViewById(R.id.etusn)
        etpass = findViewById(R.id.etpass)
        btlogin = findViewById(R.id.btlogin)
    }

    fun login() {
        btlogin.setOnClickListener {
            val username = etusn.text.toString().trim()
            val password = etpass.text.toString().trim()

            if (username.isEmpty()) {
                etusn.error = getString(R.string.ValidasiUsernameLogin)
                etusn.requestFocus()
            } else if (password.isEmpty()) {
                etpass.error = getString(R.string.ValidasiPasswordLogin)
                etpass.requestFocus()
            } else {
                val intent = Intent(this, laundry::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}