package com.putradwicahyono.laundry.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.putradwicahyono.laundry.R

class data_pegawai : AppCompatActivity() {

    lateinit var fab_tambah_pegawai : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        init()
        pindah()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init(){
        fab_tambah_pegawai = findViewById(R.id.fab_tambah_pegawai)
    }
    private fun pindah(){
        fab_tambah_pegawai.setOnClickListener{
            val intent = Intent(this, tambah_pegawai::class.java)
            startActivity(intent)
        }
    }
}