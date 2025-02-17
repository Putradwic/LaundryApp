package com.putradwicahyono.laundry.pelanggan

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan

class tambah_pelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var tvid_pelanggan : TextView
    lateinit var etnama_pelanggan : EditText
    lateinit var etalamat_pelanggan : EditText
    lateinit var etnohp_pelanggan : EditText
    lateinit var etnama_cabang : EditText
    lateinit var bttambah : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)
        init()
        bttambah.setOnClickListener{
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tvid_pelanggan = findViewById(R.id.tvid_pelanggan)
        etnama_pelanggan = findViewById(R.id.etnama_pelanggan)
        etalamat_pelanggan = findViewById(R.id.etalamat_pelanggan)
        etnohp_pelanggan = findViewById(R.id.etnohp_pelanggan)
        etnama_cabang = findViewById(R.id.etnama_cabang)
        bttambah = findViewById(R.id.bttambah)
    }

    fun cekValidasi() {
        val nama = etnama_pelanggan.text.toString()
        val alamat = etalamat_pelanggan.text.toString()
        val noHp = etnohp_pelanggan.text.toString()
        val cabang = etnama_cabang.text.toString()

        if (nama.isEmpty() || alamat.isEmpty() || noHp.isEmpty() || cabang.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }
        simpan()
    }

    fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganid = pelangganBaru.key
        val data = ModelPelanggan(
            pelangganid.toString(),
            etnama_pelanggan.text.toString(),
            etalamat_pelanggan.text.toString(),
            etnohp_pelanggan.text.toString(),
            etnama_cabang.text.toString()
        )
        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show()
            }
    }
}
