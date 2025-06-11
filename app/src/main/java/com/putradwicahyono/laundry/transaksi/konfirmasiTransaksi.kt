package com.putradwicahyono.laundry.transaksi

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.konfirmasi_tambahan_adapter
import com.putradwicahyono.laundry.model_data.ModelTambahan
import java.text.NumberFormat
import java.util.*

class konfirmasiTransaksi : AppCompatActivity() {

    private lateinit var adapterTambahan: konfirmasi_tambahan_adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView

    private var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_konfirmasi_transaksi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.konfirmasi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TextView utama
        val tvNamaPelanggan = findViewById<TextView>(R.id.tvnama_pelanggan)
        val tvNoHp = findViewById<TextView>(R.id.tvnohp_pelanggan)
        val tvNamaLayanan = findViewById<TextView>(R.id.tvnama_layanan)
        val tvHargaLayanan = findViewById<TextView>(R.id.tvharga_layanan)
        totalTextView = findViewById(R.id.totalbayar)

        // Ambil data intent
        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "-"
        val noHp = intent.getStringExtra("noHp") ?: "-"
        val namaLayanan = intent.getStringExtra("namaLayanan") ?: "-"
        val hargaLayanan = intent.getIntExtra("hargaLayanan", 0)
        val listTambahan =
            intent.getSerializableExtra("listTambahan") as? ArrayList<ModelTambahan> ?: arrayListOf()

        // Format harga
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val hargaFormatted = formatter.format(hargaLayanan)

        // Set ke TextView
        tvNamaPelanggan.text = "Nama: $namaPelanggan"
        tvNoHp.text = "No. HP: $noHp"
        tvNamaLayanan.text = "Layanan: $namaLayanan"
        tvHargaLayanan.text = "Harga: $hargaFormatted"

        // Set harga awal
        totalHarga = hargaLayanan

        // Setup RecyclerView
        adapterTambahan = konfirmasi_tambahan_adapter(listTambahan) { tambahan ->
            totalHarga -= tambahan.harga_tambahan ?: 0
            listTambahan.remove(tambahan)
            adapterTambahan.notifyDataSetChanged()
            updateTotal()
        }



        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterTambahan

        // Hitung total awal
        listTambahan.forEach {
            totalHarga += it.harga_tambahan ?: 0
        }
        updateTotal()

        // Tombol proses dan tambahan (kalau mau dihandle)
        val btProses = findViewById<Button>(R.id.btproses)
        val btTambahan = findViewById<Button>(R.id.bttambahan)

        btProses.setOnClickListener {

        }

        btTambahan.setOnClickListener {

        }
    }

    private fun updateTotal() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        totalTextView.text = formatter.format(totalHarga)
    }
}
