package com.putradwicahyono.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    lateinit var backarrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        enableEdgeToEdge()

        init()
        simpan()
        back()

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
        backarrow = findViewById(R.id.backarrow)
    }

    // Fungsi Simpan Data ke Firebase
    fun simpan() {
        btSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val noHP = etNoHP.text.toString()
            val cabang = etCabang.text.toString()

            if (nama.isEmpty()) {
                etNama.error = getString(R.string.ValidasiNamaPelanggan)
                etNama.requestFocus()
            } else if (alamat.isEmpty()) {
                etAlamat.error = getString(R.string.ValidasiAlamatPelanggan)
                etAlamat.requestFocus()
            }else if (noHP.isEmpty()) {
                etNoHP.error = getString(R.string.ValidasiNoHPPelanggan)
                etNoHP.requestFocus()
            }else if (cabang.isEmpty()) {
                etCabang.error = getString(R.string.ValidasiCabangPelanggan)
                etCabang.requestFocus()
            }else {
                val pelangganBaru = myRef.push()
                val pelangganId = pelangganBaru.key ?: "Unknown"
                val data = ModelPelanggan(pelangganId, nama, alamat, noHP, cabang,)

                pelangganBaru.setValue(data)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            this.getString(R.string.berhasil_tambah_pelanggan),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, data_pelanggan::class.java)
                        startActivity(intent)
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
    fun back(){
        backarrow.setOnClickListener {
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
            finish()
        }
    }
}
