package com.putradwicahyono.laundry.cabang

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.putradwicahyono.laundry.R
import com.google.firebase.database.FirebaseDatabase
import com.putradwicahyono.laundry.model_data.ModelCabang

class tambah_cabang : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")

    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etAlamat: EditText
    lateinit var etNoHP: EditText
    lateinit var etstatus: EditText
    lateinit var btSimpan: Button
    lateinit var backarrow: ImageView

    var editMode = false
    var isEditable = false
    var cabangId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_cabang)
        enableEdgeToEdge()

        init()
        handleForm()
        back()
        intentData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_cabang)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, data_cabang::class.java)
        startActivity(intent)
        finish()
    }

    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_cabang)
        etNama = findViewById(R.id.etnama_cabang)
        etAlamat = findViewById(R.id.etalamat_cabang)
        etNoHP = findViewById(R.id.etnohp_cabang)
        etstatus = findViewById(R.id.etstatus)
        btSimpan = findViewById(R.id.bttambah)
        backarrow = findViewById(R.id.backarrow)
    }

    fun disableEdit(enabled: Boolean) {
        etNama.isEnabled = enabled
        etAlamat.isEnabled = enabled
        etNoHP.isEnabled = enabled
        etstatus.isEnabled = enabled
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
                val status = etstatus.text.toString()

                if (nama.isEmpty()) {
                    etNama.error = getString(R.string.ValidasiNamaPegawai)
                    etNama.requestFocus()
                } else if (alamat.isEmpty()) {
                    etAlamat.error = getString(R.string.ValidasiAlamatPegawai)
                    etAlamat.requestFocus()
                } else if (noHP.isEmpty()) {
                    etNoHP.error = getString(R.string.ValidasiNoHPPegawai)
                    etNoHP.requestFocus()
                } else if (status.isEmpty()) {
                    etstatus.error = getString(R.string.ValidasiStatusPegawai)
                    etstatus.requestFocus()
                } else {
                    if (editMode && cabangId != null) {
                        val updated = ModelCabang(
                            cabangId!!, nama, alamat, noHP, status,
                        )

                        myRef.child(cabangId!!).setValue(updated)
                            .addOnSuccessListener {
                                Toast.makeText(this, this.getString(R.string.BerhasilUpdateCabang), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_cabang::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, this.getString(R.string.GagalUpdateCabang), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_cabang::class.java)
                                startActivity(intent)
                                finish()
                            }
                    } else {
                        val cabangBaru = myRef.push()
                        val cabangId = cabangBaru.key ?: "Unknown"
                        val data = ModelCabang(
                            cabangId, nama, alamat, noHP, status
                        )

                        cabangBaru.setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, getString(R.string.BerhasilTambahCabang), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_cabang::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, getString(R.string.GagalTambahCabang), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, data_cabang::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun intentData() {
        val edit = intent.getBooleanExtra("edit", false)

        if (edit) {
            editMode = true
            cabangId = intent.getStringExtra("id") ?: ""
            tvJudul.text = getString(R.string.EditCabang)
            btSimpan.text = getString(R.string.Edit)
            disableEdit(false)

            etNama.setText(intent.getStringExtra("nama") ?: "")
            etAlamat.setText(intent.getStringExtra("alamat") ?: "")
            etNoHP.setText(intent.getStringExtra("nohp") ?: "")
            etstatus.setText(intent.getStringExtra("status") ?: "")
        } else {
            tvJudul.text = getString(R.string.TambahCabangBaru)
            btSimpan.text = getString(R.string.add)
            disableEdit(true)
        }
    }
}
