package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.pelanggan.pilih_pelanggan

class transaksi : AppCompatActivity() {

    private lateinit var namaPelangganText: TextView
    private lateinit var noHPPelangganText: TextView

    private val pilihPelangganLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nama = data?.getStringExtra("nama")
            val hp = data?.getStringExtra("hp")
            namaPelangganText.text = "Nama Pelanggan: $nama"
            noHPPelangganText.text = "No HP: $hp"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        namaPelangganText = findViewById(R.id.tvnama_pelanggan)
        noHPPelangganText = findViewById(R.id.tvnohp_pelanggan)

        val btnPilihPelanggan = findViewById<Button>(R.id.btpilihpelanggan)
        btnPilihPelanggan.setOnClickListener {
            val intent = Intent(this, pilih_pelanggan::class.java)
            pilihPelangganLauncher.launch(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
