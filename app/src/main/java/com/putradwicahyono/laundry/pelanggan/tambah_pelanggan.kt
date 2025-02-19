package com.putradwicahyono.laundry.pelanggan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import com.google.firebase.database.FirebaseDatabase

class tambah_pelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

    // Inisialisasi View
    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etAlamat: EditText
    lateinit var etNoHP: EditText
    lateinit var etCabang: EditText
    lateinit var btSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        enableEdgeToEdge()
        init()
        simpan()

        // Mengatur Inset untuk StatusBar & NavigationBar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Inisialisasi View dari XML
    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_pelanggan)
        etNama = findViewById(R.id.etnama_pelanggan)
        etAlamat = findViewById(R.id.etalamat_pelanggan)
        etNoHP = findViewById(R.id.etnohp_pelanggan)
        etCabang = findViewById(R.id.etnama_cabang)
        btSimpan = findViewById(R.id.bttambah)
    }

    // Fungsi Simpan Data ke Firebase
    fun simpan() {
        btSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val noHP = etNoHP.text.toString()
            val cabang = etCabang.text.toString()

            if (nama.isEmpty() || alamat.isEmpty() || noHP.isEmpty() || cabang.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pelangganBaru = myRef.push()
            val pelangganId = pelangganBaru.key ?: "Unknown"
            val timestamp = System.currentTimeMillis().toString()
            val data = ModelPelanggan(pelangganId, nama, alamat, noHP, cabang, timestamp)

            pelangganBaru.setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        this.getString(R.string.berhasil_tambah_pelanggan),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        this.getString(R.string.pelanggan_tambah_gagal),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
