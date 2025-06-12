package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var backarrow: ImageView // Added backarrow initialization

    private var totalHarga = 0

    // Variables to hold the data received from previous activity
    private var namaPelanggan: String = ""
    private var noHp: String = ""
    private var namaLayanan: String = ""
    private var hargaLayanan: Int = 0
    private var listTambahan: ArrayList<ModelTambahan> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_konfirmasi_transaksi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.konfirmasi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews() // Initialize views
        getDataFromIntent() // Retrieve data from intent
        setupDisplay() // Setup UI with received data
        setupRecyclerView() // Setup RecyclerView for additional services
        setupButtons() // Setup button click listeners
        back() // Handle back button

    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvtambahan)
        totalTextView = findViewById(R.id.totalbayar)
        backarrow = findViewById(R.id.backarrow) // Initialize backarrow
    }

    private fun getDataFromIntent() {
        namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "-"
        noHp = intent.getStringExtra("noHp") ?: "-"
        namaLayanan = intent.getStringExtra("namaLayanan") ?: "-"
        hargaLayanan = intent.getIntExtra("hargaLayanan", 0)
        listTambahan = intent.getSerializableExtra("listTambahan") as? ArrayList<ModelTambahan> ?: arrayListOf()
    }

    private fun setupDisplay() {
        val tvNamaPelanggan = findViewById<TextView>(R.id.tvnama_pelanggan)
        val tvNoHp = findViewById<TextView>(R.id.tvnohp_pelanggan)
        val tvNamaLayanan = findViewById<TextView>(R.id.tvnama_layanan)
        val tvHargaLayanan = findViewById<TextView>(R.id.tvharga_layanan)

        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val hargaFormatted = formatter.format(hargaLayanan)

        tvNamaPelanggan.text = "${getString(R.string.Nama)} : $namaPelanggan"
        tvNoHp.text = "${getString(R.string.NoHP)} : $noHp"
        tvNamaLayanan.text = "${getString(R.string.Layanan)} : $namaLayanan"
        tvHargaLayanan.text = "${getString(R.string.Harga)} : $hargaFormatted"

        totalHarga = hargaLayanan // Initialize total price with main service price
        listTambahan.forEach {
            totalHarga += it.harga_tambahan ?: 0
        }
        updateTotal()
    }

    private fun setupRecyclerView() {
        adapterTambahan = konfirmasi_tambahan_adapter(listTambahan) { tambahan ->
            // Optionally, you can enable removal of additional services here if needed.
            // For now, in a confirmation screen, we might not want to remove items directly.
            // If you want to allow removal, uncomment the following lines and handle UI updates.
            // totalHarga -= tambahan.harga_tambahan ?: 0
            // listTambahan.remove(tambahan)
            // adapterTambahan.notifyDataSetChanged()
            // updateTotal()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterTambahan
    }

    private fun setupButtons() {
        val btProses = findViewById<Button>(R.id.btproses)
        val btTambahan = findViewById<Button>(R.id.bttambahan) // This is the "Batal" button in your layout.

            btProses.setOnClickListener {
                // Logic to save the transaction to Firebase or local database
                // ... (Add your transaction saving logic here) ...

                // Then, navigate to InvoiceActivity
                val intent = Intent(this, Invoice::class.java).apply {
                    putExtra("namaPelanggan", namaPelanggan)
                    putExtra("noHp", noHp)
                    putExtra("namaLayanan", namaLayanan)
                    putExtra("hargaLayanan", hargaLayanan)
                    putExtra("listTambahan", listTambahan) // Pass the entire list
                }
                startActivity(intent)
                finish()
            }

        btTambahan.setOnClickListener { // This is the "Batal" button
            // Navigate back to the previous activity (transaksi)
            onBackPressed()
            finish()

        }
    }

    private fun updateTotal() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        totalTextView.text = formatter.format(totalHarga)
    }

    private fun back() {
        backarrow.setOnClickListener {
            onBackPressed()
            finish()// Go back to the previous activity (transaksi)
        }
    }
}