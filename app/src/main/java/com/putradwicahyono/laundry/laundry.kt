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
import android.widget.Toast
import com.google.firebase.database.*
import com.putradwicahyono.laundry.layanan.data_layanan
import com.putradwicahyono.laundry.transaksi.transaksi


class laundry : AppCompatActivity() {

    lateinit var card_pelanggan: CardView
    lateinit var card_pegawai: CardView
    lateinit var cardlayanan: CardView
    lateinit var card_transaksi: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_laundry)

        val tv_sambutan : TextView = findViewById(R.id.tv_sambutan)
        val tv_tanggal : TextView = findViewById(R.id.tv_tanggal)

        val waktu_sekarang = Calendar.getInstance()
        val jam_sekarang = waktu_sekarang.get(Calendar.HOUR_OF_DAY)

        val sambutan = when {
            jam_sekarang in 0..10 -> getString(R.string.SelamatPagi)
            jam_sekarang in 11..14 -> getString(R.string.SelamatSiang)
            jam_sekarang in 15..18 -> getString(R.string.SelamatSore)
            else -> getString(R.string.SelamatMalam)
        }

        tv_sambutan.text = sambutan

        val locale = Locale.getDefault()
        val tanggal_format = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)
        val tanggal_sekarang = tanggal_format.format(Date())
        tv_tanggal.text = tanggal_sekarang

        val sloganlist = arrayOf(
            getString(R.string.Slogan1),
            getString(R.string.Slogan2),
            getString(R.string.Slogan3)
        )

        val tvslogan = findViewById<TextView>(R.id.tvslogan)
        slogan(tvslogan, sloganlist)


        init()
        pindah()

        val myRef = FirebaseDatabase.getInstance().getReference("test_connection")

        myRef.setValue("Cek koneksi Firebase!")
            .addOnSuccessListener {
                Log.d("FirebaseCheck", "Firebase sudah terhubung!")
            }
            .addOnFailureListener {
                Log.e("FirebaseCheck", "Gagal terhubung ke Firebase", it)
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.laundry)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        card_pelanggan = findViewById(R.id.card_pelanggan)
        card_pegawai = findViewById(R.id.cardpegawai)
        cardlayanan = findViewById(R.id.cardlayanan)
        card_transaksi = findViewById(R.id.card_transaksi)
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
        cardlayanan.setOnClickListener{
            val intent = Intent(this, data_layanan::class.java)
            startActivity(intent)
        }
        card_transaksi.setOnClickListener{
            val intent = Intent(this, transaksi::class.java)
            startActivity(intent)
        }

    }
    private var jedaWaktuTekan: Long = 0
    private lateinit var toast: Toast

    override fun onBackPressed() {
        if (jedaWaktuTekan + 2000 > System.currentTimeMillis()) {
            toast.cancel()
            super.onBackPressed()
        } else {
            toast = Toast.makeText(this, getString(R.string.KeluarApp), Toast.LENGTH_SHORT)
            toast.show()
        }
        jedaWaktuTekan = System.currentTimeMillis()
    }

    fun slogan(textView: TextView, texts: Array<String>) {
        val randomText = texts.random()
        textView.text = randomText
    }

}