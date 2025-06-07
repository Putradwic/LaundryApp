package com.putradwicahyono.laundry.pelanggan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import com.google.firebase.database.FirebaseDatabase
import com.putradwicahyono.laundry.layanan.data_layanan

class tambah_pelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etAlamat: EditText
    lateinit var etNoHP: EditText
    lateinit var etCabang: EditText
    lateinit var btSimpan: Button
    lateinit var backarrow: ImageView

    var editMode = false
    var isEditable = false
    var pelangganId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)
        enableEdgeToEdge()

        init()
        handleForm()
        back()
        intentData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, data_pelanggan::class.java)
        startActivity(intent)
        finish()
    }

    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_pelanggan)
        etNama = findViewById(R.id.etnama_pelanggan)
        etAlamat = findViewById(R.id.etalamat_pelanggan)
        etNoHP = findViewById(R.id.etnohp_pelanggan)
        etCabang = findViewById(R.id.etnama_cabang)
        btSimpan = findViewById(R.id.bttambah)
        backarrow = findViewById(R.id.backarrow)
    }

    fun disableEdit(enabled: Boolean) {
        etNama.isEnabled = enabled
        etAlamat.isEnabled = enabled
        etNoHP.isEnabled = enabled
        etCabang.isEnabled = enabled
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
                val alamat = etAlamat.text.toString()
                val noHP = etNoHP.text.toString()
                val cabang = etCabang.text.toString()

                if (nama.isEmpty()) {
                    etNama.error = getString(R.string.ValidasiNamaPelanggan)
                    etNama.requestFocus()
                } else if (alamat.isEmpty()) {
                    etAlamat.error = getString(R.string.ValidasiAlamatPelanggan)
                    etAlamat.requestFocus()
                } else if (noHP.isEmpty()) {
                    etNoHP.error = getString(R.string.ValidasiNoHPPelanggan)
                    etNoHP.requestFocus()
                } else if (cabang.isEmpty()) {
                    etCabang.error = getString(R.string.ValidasiCabangPelanggan)
                    etCabang.requestFocus()
                } else {
                    if (editMode && pelangganId != null) {
                        val updated = ModelPelanggan(
                            pelangganId!!, nama, alamat, noHP, cabang, System.currentTimeMillis()
                        )

                        myRef.child(pelangganId!!).setValue(updated)
                            .addOnSuccessListener {
                                Toast.makeText(this, this.getString(R.string.BerhasilUpdatePelanggan), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_pelanggan::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, this.getString(R.string.GagalUpdatePelanggan), Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        val pelangganBaru = myRef.push()
                        val pelangganId = pelangganBaru.key ?: "Unknown"
                        val data = ModelPelanggan(
                            pelangganId, nama, alamat, noHP, cabang, System.currentTimeMillis()
                        )

                        pelangganBaru.setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, getString(R.string.BerhasilTambahPelanggan), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_pelanggan::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, getString(R.string.GagalTambahPelanggan), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun intentData() {
        val edit = intent.getBooleanExtra("edit", false)

        if (edit) {
            editMode = true
            pelangganId = intent.getStringExtra("id") ?: ""
            tvJudul.text = getString(R.string.EditPelanggan)
            btSimpan.text = getString(R.string.Edit)
            disableEdit(false)

            etNama.setText(intent.getStringExtra("nama") ?: "")
            etAlamat.setText(intent.getStringExtra("alamat") ?: "")
            etNoHP.setText(intent.getStringExtra("nohp") ?: "")
            etCabang.setText(intent.getStringExtra("cabang") ?: "")
        } else {
            tvJudul.text = getString(R.string.TambahPelangganBaru)
            btSimpan.text = getString(R.string.add)
            disableEdit(true)
        }
    }
}
