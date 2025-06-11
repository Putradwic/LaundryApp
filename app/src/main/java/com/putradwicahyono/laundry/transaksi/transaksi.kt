package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.dipilih_tambahan_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.model_data.ModelTambahan

class transaksi : AppCompatActivity() {


    private lateinit var backarrow: ImageView
    private lateinit var namaPelangganText: TextView
    private lateinit var noHPPelangganText: TextView
    private lateinit var namaLayananText: TextView
    private lateinit var hargaLayananText: TextView

    private val pilihPelangganLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> handlePilihPelangganResult(result)}

    private val pilihLayananLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> handlePilihLayananResult(result)}

    private val pilihTambahanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> handlePilihTambahanResult(result) }


    private val listTambahanDipilih = mutableListOf<ModelTambahan>()

    private lateinit var tambahanAdapter: dipilih_tambahan_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        initView()
        setupClickListeners()
        applyWindowInsets()
        back()

        val recyclerView = findViewById<RecyclerView>(R.id.rvtambahan)
        tambahanAdapter = dipilih_tambahan_adapter(listTambahanDipilih) { tambahan ->
            listTambahanDipilih.remove(tambahan)
            tambahanAdapter.notifyDataSetChanged()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tambahanAdapter

    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transaksi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView() {
        backarrow = findViewById(R.id.backarrow)
        namaPelangganText = findViewById(R.id.tvnama_pelanggan)
        noHPPelangganText = findViewById(R.id.tvnohp_pelanggan)
        namaLayananText = findViewById(R.id.tvnama_layanan)
        hargaLayananText = findViewById(R.id.tvharga_layanan)
    }
    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, laundry::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.btpilihpelanggan).setOnClickListener {
            val intent = Intent(this, Pilih_pelanggan::class.java)
            pilihPelangganLauncher.launch(intent)
        }
        findViewById<Button>(R.id.btpilihlayanan).setOnClickListener {
            val intent = Intent(this, Pilih_layanan::class.java)
            pilihLayananLauncher.launch(intent)
        }
        findViewById<Button>(R.id.bttambahan).setOnClickListener {
            val intent = Intent(this, Pilih_tambahan::class.java)
            pilihTambahanLauncher.launch(intent)
        }
//        findViewById<Button>(R.id.btproses).setOnClickListener {
//            val intent = Intent(this, konfirmasiTransaksi::class.java)
//            intent.putExtra("list_tambahan", ArrayList(listTambahanDipilih))
//            startActivity(intent)
//        }
    }

    private fun handlePilihPelangganResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nama = data?.getStringExtra("nama")
            val hp = data?.getStringExtra("nohp")

            if (!nama.isNullOrEmpty() && !hp.isNullOrEmpty()) {
                namaPelangganText.text = "${getString(R.string.Nama)} : $nama"
                noHPPelangganText.text = "${getString(R.string.NoHP)} : $hp"
            } else {
                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handlePilihLayananResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val namalayanan = data?.getStringExtra("nama")
            val harga = data?.getIntExtra("harga", -1)

            if (!namalayanan.isNullOrEmpty() && harga != -1) {
                namaLayananText.text = "${getString(R.string.Layanan)} : $namalayanan"

                val hargaFormatted = "Rp %,d".format(harga).replace(',', '.')
                hargaLayananText.text = "${getString(R.string.Harga)} : $hargaFormatted"

            } else {
                Toast.makeText(this, "Gagal mengambil data layanan", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun handlePilihTambahanResult(result: androidx.activity.result.ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val id = data?.getStringExtra("idTambahan")
            val nama = data?.getStringExtra("nama")
            val harga = data?.getIntExtra("harga", 0)

            if (!id.isNullOrEmpty() && !nama.isNullOrEmpty()) {
                val tambahan = ModelTambahan(id, nama, harga ?: 0)
                listTambahanDipilih.add(tambahan)
                tambahanAdapter.notifyItemInserted(listTambahanDipilih.size - 1)
            } else {
                Toast.makeText(this, "Data tambahan kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
