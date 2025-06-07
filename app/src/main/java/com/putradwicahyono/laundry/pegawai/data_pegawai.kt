package com.putradwicahyono.laundry.pegawai

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
import com.putradwicahyono.laundry.laundry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class data_pegawai : AppCompatActivity() {

    // Inisialisasi Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")

    // Deklarasi variabel untuk RecyclerView dan FloatingActionButton
    private lateinit var rvdatapegawai: RecyclerView
    private lateinit var fab_tambah_pegawai: FloatingActionButton
    private var listPegawai = arrayListOf<ModelPegawai>()
    private lateinit var backarrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pegawai)
        enableEdgeToEdge()

        //Memanggil data
        initViews()
        setupRecyclerView()
        setupListeners()
        getData()
        back()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pegawai)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    // Inisialisasi variabel dengan elemen dari layout XML
    private fun initViews() {
        rvdatapegawai = findViewById(R.id.rvdatapegawai)
        fab_tambah_pegawai = findViewById(R.id.fab_tambah_pegawai)
        backarrow = findViewById(R.id.backarrow)
    }

    // Mengatur tampilan RecyclerView list
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatapegawai.layoutManager = layoutManager
        rvdatapegawai.setHasFixedSize(true)
    }

    // Menangani event click
    private fun setupListeners() {
        fab_tambah_pegawai.setOnClickListener {
            val intent = Intent(this, tambah_pegawai::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ambil data dari firebase
    private fun getData() {
        val query = myRef.orderByChild("idPegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPegawai.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(ModelPegawai::class.java)
                        pegawai?.let { listPegawai.add(it) }
                    }
                    val adapter = data_pegawai_adapter(listPegawai) { pegawai ->
                        munculDialogPegawai(pegawai)
                    }
                    rvdatapegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            // Penanganan eror
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_pegawai, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Memunculkan dialog mod berdasarkan layout
    fun munculDialogPegawai(pegawai: ModelPegawai) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_mod_pegawai, null)
        view.findViewById<TextView>(R.id.tvnama_pegawai).text = pegawai.nama_pegawai
        view.findViewById<TextView>(R.id.tvalamat_pegawai).text = pegawai.alamat_pegawai
        view.findViewById<TextView>(R.id.tvnohp_pegawai).text = pegawai.nohp_pegawai
        view.findViewById<TextView>(R.id.tvcabang_pegawai).text = pegawai.cabang
        view.findViewById<TextView>(R.id.tvid_pegawai).text = pegawai.id_pegawai
        view.findViewById<TextView>(R.id.status).text = pegawai.status
        val tanggalFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val tanggal = tanggalFormat.format(Date(pegawai.timestamp ?: 0))
        view.findViewById<TextView>(R.id.terdaftar).text = tanggal
        val btnSunting = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .show()

        btnSunting.setOnClickListener {
            val intent = Intent(this, tambah_pegawai::class.java).apply {
                putExtra("edit", true)
                putExtra("id", pegawai.id_pegawai)
                putExtra("nama", pegawai.nama_pegawai)
                putExtra("alamat", pegawai.alamat_pegawai)
                putExtra("nohp", pegawai.nohp_pegawai)
                putExtra("cabang", pegawai.cabang)
                putExtra("status", pegawai.status)
            }
            this.startActivity(intent)
            dialog.dismiss()
            finish()
        }
        btnHapus.setOnClickListener {
            pegawai.id_pegawai?.let { id ->
                konfrimasi(
                    title = getString(R.string.HapusPegawai),
                    message = "${getString(R.string.KonfirmasiHapus1)}, ${pegawai.nama_pegawai} ?",
                    onConfirm = {
                        deletePegawai(id)
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
    private fun deletePegawai(pegawaiId: String) {
        myRef.child(pegawaiId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.Hapusgagal1), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, data_pegawai::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil1), Toast.LENGTH_SHORT).show()
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
