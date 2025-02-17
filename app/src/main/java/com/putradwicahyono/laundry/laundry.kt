package com.putradwicahyono.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.pegawai.data_pegawai
import com.putradwicahyono.laundry.pelanggan.data_pelanggan
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import com.google.firebase.database.*

class laundry : AppCompatActivity() {

    lateinit var card_pelanggan: CardView
    lateinit var card_pegawai: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_laundry)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        val tv_sambutan : TextView = findViewById(R.id.tv_sambutan)
        val tv_tanggal : TextView = findViewById(R.id.tv_tanggal)

        val waktu_sekarang = Calendar.getInstance()
        val jam_sekarang = waktu_sekarang.get(Calendar.HOUR_OF_DAY)

        val sambutan = when {
            jam_sekarang in 0..10 -> "Selamat Pagi, Putra"
            jam_sekarang in 11..14 -> "Selamat Siang, Putra"
            jam_sekarang in 15..18 -> "Selamat Sore, Putra"
            else -> "Selamat Malam, Putra"
        }

        tv_sambutan.text = sambutan

        val tanggal_format = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        val tanggal_sekarang = tanggal_format.format(Date())
        tv_tanggal.text = tanggal_sekarang

        init()
        pindah()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("test_connection")

        myRef.setValue("Cek koneksi Firebase!")
            .addOnSuccessListener {
                Log.d("FirebaseCheck", "Firebase sudah terhubung!")
            }
            .addOnFailureListener {
                Log.e("FirebaseCheck", "Gagal terhubung ke Firebase", it)
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        card_pelanggan = findViewById(R.id.card_pelanggan)
        card_pegawai = findViewById(R.id.cardpegawai)

    }
    private fun pindah(){
        card_pelanggan.setOnClickListener{
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
        }
        card_pegawai.setOnClickListener{
            val intent = Intent(this, data_pegawai::class.java)
            startActivity(intent)
        }

    }
}