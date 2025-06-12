package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
        adapterTambahan = konfirmasi_tambahan_adapter(listTambahan)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterTambahan
    }

    private fun setupButtons() {
        val btProses = findViewById<Button>(R.id.btproses)
        val btTambahan = findViewById<Button>(R.id.bttambahan) // This is the "Batal" button in your layout.

        btProses.setOnClickListener {
            showPaymentDialog()
        }

        btTambahan.setOnClickListener { // This is the "Batal" button
            // Navigate back to the previous activity (transaksi)
            onBackPressed()
            finish()

        }
    }

    private fun showPaymentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.cardmetode, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Set dialog window properties for better appearance
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Get button references
        val btnQris = dialogView.findViewById<Button>(R.id.btn_qris)
        val btnBayarNanti = dialogView.findViewById<Button>(R.id.btn_bayar_nanti)
        val btnTunai = dialogView.findViewById<Button>(R.id.btn_tunai)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        // QRIS button click
        btnQris.setOnClickListener {
            dialog.dismiss()
            goToInvoice("QRIS")
        }

        // Bayar Nanti button click
        btnBayarNanti.setOnClickListener {
            dialog.dismiss()
            goToInvoice("Bayar Nanti")
        }

        // Tunai button click
        btnTunai.setOnClickListener {
            dialog.dismiss()
            goToInvoice("Tunai")
        }

        // Cancel button click
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun goToInvoice(metodePembayaran: String) {
        val intent = Intent(this, Invoice::class.java).apply {
            putExtra("namaPelanggan", namaPelanggan)
            putExtra("noHp", noHp)
            putExtra("namaLayanan", namaLayanan)
            putExtra("hargaLayanan", hargaLayanan)
            putExtra("listTambahan", listTambahan)
            putExtra("metodePembayaran", metodePembayaran) // Tambah metode pembayaran
        }
        startActivity(intent)
        finish()
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