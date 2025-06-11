package com.putradwicahyono.laundry.cabang

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.data_pegawai_adapter
import com.putradwicahyono.laundry.model_data.ModelPegawai
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putradwicahyono.laundry.adapter.data_cabang_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.model_data.ModelCabang
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class data_cabang : AppCompatActivity() {

    // Inisialisasi Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("cabang")

    // Deklarasi variabel untuk RecyclerView dan FloatingActionButton
    private lateinit var rvdatacabang: RecyclerView
    private lateinit var fab_tambah_cabang: FloatingActionButton
    private var listCabang = arrayListOf<ModelCabang>()
    private lateinit var backarrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cabang)
        enableEdgeToEdge()

        //Memanggil data
        initViews()
        setupRecyclerView()
        setupListeners()
        getData()
        back()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_cabang)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    // Inisialisasi variabel dengan elemen dari layout XML
    private fun initViews() {
        rvdatacabang = findViewById(R.id.rvdatacabang)
        fab_tambah_cabang = findViewById(R.id.fab_tambah)
        backarrow = findViewById(R.id.backarrow)
    }

    // Mengatur tampilan RecyclerView list
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatacabang.layoutManager = layoutManager
        rvdatacabang.setHasFixedSize(true)
    }

    // Menangani event click
    private fun setupListeners() {
        fab_tambah_cabang.setOnClickListener {
            val intent = Intent(this, tambah_cabang::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ambil data dari firebase
    private fun getData() {
        val query = myRef.orderByChild("idCabang").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listCabang.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val cabang = dataSnapshot.getValue(ModelCabang::class.java)
                        cabang?.let { listCabang.add(it) }
                    }
                    val adapter = data_cabang_adapter(listCabang) { cabang ->
                        munculDialogCabang(cabang)
                    }
                    rvdatacabang.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            // Penanganan eror
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_cabang, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Memunculkan dialog mod berdasarkan layout
    fun munculDialogCabang(cabang: ModelCabang) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_mod_cabang, null)
        view.findViewById<TextView>(R.id.tvnama_cabang).text = cabang.nama_cabang
        view.findViewById<TextView>(R.id.tvalamat_cabang).text = cabang.alamat_cabang
        view.findViewById<TextView>(R.id.tvnohp_cabang).text = cabang.nohp_cabang
        view.findViewById<TextView>(R.id.tvid_cabang).text = cabang.id_cabang
        view.findViewById<TextView>(R.id.tvstatus).text = cabang.status
        val btnSunting = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .show()

        btnSunting.setOnClickListener {
            val intent = Intent(this, tambah_cabang::class.java).apply {
                putExtra("edit", true)
                putExtra("id", cabang.id_cabang)
                putExtra("nama", cabang.nama_cabang)
                putExtra("alamat", cabang.alamat_cabang)
                putExtra("nohp", cabang.nohp_cabang)
                putExtra("status", cabang.status)
            }
            this.startActivity(intent)
            dialog.dismiss()
            finish()
        }
        btnHapus.setOnClickListener {
            cabang.id_cabang?.let { id ->
                konfrimasi(
                    title = getString(R.string.HapusCabang),
                    message = "${getString(R.string.KonfirmasiHapus3)}, ${cabang.nama_cabang} ?",
                    onConfirm = {
                        deleteCabang(id)
                    }
                )
            } ?: Toast.makeText(this, getString(R.string.IDTidakAda), Toast.LENGTH_SHORT).show()
        }


    }
    // Fungsi untuk memunculkan dialog dengan perubahan minor
    fun konfrimasi(
        title: String,
        message: String,
        onConfirm: () -> Unit
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_konfirmasi, null)

        val tvTitle = view.findViewById<TextView>(R.id.modalJudul)
        val tvMessage = view.findViewById<TextView>(R.id.pesan)
        val btnCancel = view.findViewById<Button>(R.id.btnbatal)
        val btnConfirm = view.findViewById<Button>(R.id.btnHapus)

        tvTitle.text = title
        tvMessage.text = message

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnConfirm.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    // Fungsi hapus pegawai
    private fun deleteCabang(cabangId: String) {
        myRef.child(cabangId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil3), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, data_cabang::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, this.getString(R.string.HapusGagal3), Toast.LENGTH_SHORT).show()
            }
    }

    fun back() {
        backarrow.setOnClickListener {
            val intent = Intent(this, laundry::class.java)
            startActivity(intent)
            finish()
        }
    }
}
