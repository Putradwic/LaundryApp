package com.putradwicahyono.laundry.pegawai

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
import com.putradwicahyono.laundry.model_data.ModelPegawai
import com.google.firebase.database.FirebaseDatabase

class tambah_pegawai : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")

    // Inisialisasi View
    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etAlamat: EditText
    lateinit var etNoHP: EditText
    lateinit var etCabang: EditText
    lateinit var etstatus: EditText
    lateinit var btSimpan: Button

    var idPegawai: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)

        init()
        simpan()
        btSimpan.setOnClickListener {
            getData()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_pegawai)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_pegawai)
        etNama = findViewById(R.id.etnama_pegawai)
        etAlamat = findViewById(R.id.etalamat_pegawai)
        etNoHP = findViewById(R.id.etnohp_pegawai)
        etCabang = findViewById(R.id.etnama_cabang)
        etstatus = findViewById(R.id.etstatus)
        btSimpan = findViewById(R.id.bttambah)
    }

    // Fungsi Simpan Data ke Firebase
    fun simpan() {
        btSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val noHP = etNoHP.text.toString()
            val cabang = etCabang.text.toString()
            val status = etCabang.text.toString()

            if (nama.isEmpty()) {
                etNama.error = getString(R.string.ValidasiNamaPegawai)
                etNama.requestFocus()
            } else if (alamat.isEmpty()) {
                etAlamat.error = getString(R.string.ValidasiAlamatPegawai)
                etAlamat.requestFocus()
            } else if (noHP.isEmpty()) {
                etNoHP.error = getString(R.string.ValidasiNoHPPegawai)
                etNoHP.requestFocus()
            }else if (status.isEmpty()) {
                etstatus.error = getString(R.string.ValidasiStatusPegawai)
                etstatus.requestFocus()
            } else if (cabang.isEmpty()) {
                etCabang.error = getString(R.string.ValidasiCabangPegawai)
                etCabang.requestFocus()
            }


            val pegawaiBaru = myRef.push()
            val pegawaiId = pegawaiBaru.key ?: "Unknown"
            val timestamp = System.currentTimeMillis().toString()
            val data = ModelPegawai(pegawaiId, nama, alamat, noHP, cabang, timestamp)

            pegawaiBaru.setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        this.getString(R.string.BerhasilTambahPelanggan),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        this.getString(R.string.GagalTambahPelanggan),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }




    fun getData() {
        idPegawai = intent.getStringExtra("idPegawai").toString()
        val judul = intent.getStringExtra("judul")
        val nama = intent.getStringExtra("namaPegawai")
        val alamat = intent.getStringExtra("alamatPegawai")
        val hp = intent.getStringExtra("noHPPegawai")
        val cabang = intent.getStringExtra("cabang")

        tvJudul.text = judul
        etNama.setText(nama)
        etAlamat.setText(alamat)
        etNoHP.setText(hp)
        etCabang.setText(cabang)
        if (!tvJudul.text.equals("Tambah Pegawai")) {
            if (judul.equals("Edit Pegawai"))
                mati()
            btSimpan.text = "Sunting"
        } else {
            hidup()
            etNama.requestFocus()
            btSimpan
        }
        update()

    }
    fun mati() {
        etNama.isEnabled = false
        etAlamat.isEnabled = false
        etNoHP.isEnabled = false
        etCabang.isEnabled = false
    }
    fun hidup() {
        etNama.isEnabled = true
        etAlamat.isEnabled = true
        etNoHP.isEnabled = true
        etCabang.isEnabled = true
    }

    fun update(){
        val pegawaiRef = database.getReference("Pegawai").child(idPegawai)
        val data = ModelPegawai(
            idPegawai,
            etNama.text.toString(),
            etAlamat.text.toString(),
            etNoHP.text.toString(),
            etCabang.text.toString(),)

        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"] = data.nama_pegawai.toString()
        updateData["alamatPegawai"] = data.alamat_pegawai.toString()
        updateData["noHPPegawai"] = data.nohp_pegawai.toString()
        updateData["cabang"] = data.cabang.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@tambah_pegawai, "Data Pegawai Berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnSuccessListener {
            Toast.makeText(this@tambah_pegawai, "Data Pegawai Gagal diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

}