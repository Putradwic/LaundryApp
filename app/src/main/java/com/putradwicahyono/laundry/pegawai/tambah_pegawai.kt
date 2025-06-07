package com.putradwicahyono.laundry.pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
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

    lateinit var tvJudul: TextView
    lateinit var etNama: EditText
    lateinit var etAlamat: EditText
    lateinit var etNoHP: EditText
    lateinit var etCabang: EditText
    lateinit var etstatus: EditText
    lateinit var btSimpan: Button
    lateinit var backarrow: ImageView

    var editMode = false
    var isEditable = false
    var pegawaiId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pegawai)
        enableEdgeToEdge()

        init()
        handleForm()
        back()
        intentData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_pegawai)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, data_pegawai::class.java)
        startActivity(intent)
        finish()
    }

    fun init() {
        tvJudul = findViewById(R.id.tvjudul_tambah_pegawai)
        etNama = findViewById(R.id.etnama_pegawai)
        etAlamat = findViewById(R.id.etalamat_pegawai)
        etNoHP = findViewById(R.id.etnohp_pegawai)
        etCabang = findViewById(R.id.etnama_cabang)
        etstatus = findViewById(R.id.etstatus)
        btSimpan = findViewById(R.id.bttambah)
        backarrow = findViewById(R.id.backarrow)
    }

    fun disableEdit(enabled: Boolean) {
        etNama.isEnabled = enabled
        etAlamat.isEnabled = enabled
        etNoHP.isEnabled = enabled
        etCabang.isEnabled = enabled
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
                val cabang = etCabang.text.toString()
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
                } else if (cabang.isEmpty()) {
                    etCabang.error = getString(R.string.ValidasiCabangPegawai)
                    etCabang.requestFocus()
                }else if (status.isEmpty()) {
                    etstatus.error = getString(R.string.ValidasiStatusPegawai)
                    etstatus.requestFocus()
                } else {
                    if (editMode && pegawaiId != null) {
                        val updated = ModelPegawai(
                            pegawaiId!!, nama, alamat, noHP, cabang, status,
                            System.currentTimeMillis()
                        )

                        myRef.child(pegawaiId!!).setValue(updated)
                            .addOnSuccessListener {
                                Toast.makeText(this, this.getString(R.string.BerhasilUpdatePegawai), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_pegawai::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, this.getString(R.string.GagalUpdatePegawai), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_pegawai::class.java)
                                startActivity(intent)
                                finish()
                            }
                    } else {
                        val pegawaiBaru = myRef.push()
                        val pegawaiId = pegawaiBaru.key ?: "Unknown"
                        val data = ModelPegawai(
                            pegawaiId, nama, alamat, noHP, cabang, status,
                            System.currentTimeMillis()
                        )

                        pegawaiBaru.setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, getString(R.string.BerhasilTambahPegawai), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, data_pegawai::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, getString(R.string.GagalTambahPegawai), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, data_pegawai::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun intentData() {
        val edit = intent.getBooleanExtra("edit", false)

        if (edit) {
            editMode = true
            pegawaiId = intent.getStringExtra("id") ?: ""
            tvJudul.text = getString(R.string.EditPegawai)
            btSimpan.text = getString(R.string.Edit)
            disableEdit(false)

            etNama.setText(intent.getStringExtra("nama") ?: "")
            etAlamat.setText(intent.getStringExtra("alamat") ?: "")
            etNoHP.setText(intent.getStringExtra("nohp") ?: "")
            etCabang.setText(intent.getStringExtra("cabang") ?: "")
            etstatus.setText(intent.getStringExtra("status") ?: "")
        } else {
            tvJudul.text = getString(R.string.TambahPegawaiBaru)
            btSimpan.text = getString(R.string.add)
            disableEdit(true)
        }
    }
}
