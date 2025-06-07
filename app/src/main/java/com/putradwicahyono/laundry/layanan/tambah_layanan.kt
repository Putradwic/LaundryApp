package com.putradwicahyono.laundry.layanan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelLayanan
import com.google.firebase.database.FirebaseDatabase

class tambah_layanan : AppCompatActivity() {

    // database
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")

    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etcabang: EditText
    lateinit var etHarga: EditText
    lateinit var btSimpan: Button
    lateinit var backarrow: ImageView

    var editMode = false
    var isEditable = false
    var layananId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_layanan)
        enableEdgeToEdge()

        init()
        handleForm()
        back()
        intentData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_layanan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, data_layanan::class.java)
        startActivity(intent)
        finish()
    }

    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_layanan)
        etNama = findViewById(R.id.etnama_layanan)
        etcabang = findViewById(R.id.etnama_cabang)
        etHarga = findViewById(R.id.etharga_layanan)
        btSimpan = findViewById(R.id.bttambah)
        backarrow = findViewById(R.id.backarrow)
    }

    fun disableEdit(enabled: Boolean) {
        etNama.isEnabled = enabled
        etHarga.isEnabled = enabled
        etcabang.isEnabled = enabled
        isEditable = enabled
    }

    fun handleForm() {
        btSimpan.setOnClickListener {
            if (editMode && !isEditable) {
                // Ubah jadi mode edit
                disableEdit(true)
                btSimpan.text = getString(R.string.Save)
                Toast.makeText(this, getString(R.string.Editable), Toast.LENGTH_SHORT).show()
            } else {
                // Lanjut ke proses simpan (tambah atau update)
                val nama = etNama.text.toString()
                val hargaStr = etHarga.text.toString()
                val harga = hargaStr.toIntOrNull()
                val cabang = etcabang.text.toString()

                if (nama.isEmpty()) {
                    etNama.error = getString(R.string.ValidasiNamaLayanan)
                    etNama.requestFocus()
                } else if (hargaStr.isEmpty()) {
                    etHarga.error = getString(R.string.ValidasiHargaLayanan)
                    etHarga.requestFocus()
                } else if (harga == null) {
                    etHarga.error = "Harga harus berupa angka!"
                    etHarga.requestFocus()
                } else if (cabang.isEmpty()) {
                    etcabang.error = getString(R.string.ValidasiCabangLayanan)
                    etcabang.requestFocus()
                } else {
                    if (editMode && layananId != null) {
                        val updated = ModelLayanan(
                            layananId!!, nama, harga, cabang
                        )

                        myRef.child(layananId!!).setValue(updated)
                            .addOnSuccessListener {
                                Toast.makeText(this, this.getString(R.string.BerhasilUpdateLayanan), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_layanan::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, this.getString(R.string.GagalUpdateLayanan), Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val layananBaru = myRef.push()
                        val layananId = layananBaru.key ?: "Unknown"
                        val data = ModelLayanan(
                            layananId, nama, harga, cabang
                        )

                        layananBaru.setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, getString(R.string.BerhasilTambahLayanan), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_layanan::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, getString(R.string.GagalTambahLayanan), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, data_layanan::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun intentData() {
        val edit = intent.getBooleanExtra("edit", false)

        if (edit) {
            editMode = true
            layananId = intent.getStringExtra("idLayanan") ?: ""
            tvJudul.text = getString(R.string.EditLayanan)
            btSimpan.text = getString(R.string.Edit)
            disableEdit(false)

            etNama.setText(intent.getStringExtra("nama") ?: "")
            etHarga.setText(intent.getStringExtra("harga") ?: "")
            etcabang.setText(intent.getStringExtra("cabang") ?: "")
        } else {
            tvJudul.text = getString(R.string.TambahLayananBaru)
            btSimpan.text = getString(R.string.add)
            disableEdit(true)
        }
    }


}
