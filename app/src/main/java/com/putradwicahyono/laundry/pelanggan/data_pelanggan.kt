package com.putradwicahyono.laundry.pelanggan

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
import com.putradwicahyono.laundry.adapter.data_pelanggan_adapter
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putradwicahyono.laundry.laundry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class data_pelanggan : AppCompatActivity() {

    // Inisialisasi Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    // Deklarasi variabel untuk RecyclerView dan FloatingActionButton
    private lateinit var rvdatapelanggan: RecyclerView
    private lateinit var fab_tambah_pelanggan: FloatingActionButton
    private var listPelanggan = arrayListOf<ModelPelanggan>()
    private lateinit var backarrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelanggan)
        enableEdgeToEdge()

        //Memanggil data
        initViews()
        setupRecyclerView()
        setupListeners()
        getData()
        back()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    // Inisialisasi variabel dengan elemen dari layout XML
    private fun initViews() {
        rvdatapelanggan = findViewById(R.id.rvdatapelanggan)
        fab_tambah_pelanggan = findViewById(R.id.fab_tambah_pelanggan)
        backarrow = findViewById(R.id.backarrow)
    }

    // Mengatur tampilan RecyclerView list
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatapelanggan.layoutManager = layoutManager
        rvdatapelanggan.setHasFixedSize(true)
    }

    // Menangani event click
    private fun setupListeners() {
        fab_tambah_pelanggan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ambil data dari firebase
    private fun getData() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPelanggan.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        pelanggan?.let { listPelanggan.add(it) }
                    }
                    val adapter = data_pelanggan_adapter(listPelanggan) { pelanggan ->
                        munculDialogPelanggan(pelanggan)
                    }
                    rvdatapelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            // Penanganan eror
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_pelanggan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Memunculkan dialog mod berdasarkan layout
    fun munculDialogPelanggan(pelanggan: ModelPelanggan) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_mod_pelanggan, null)
        view.findViewById<TextView>(R.id.tvnama_pelanggan).text = pelanggan.nama_pelanggan
        view.findViewById<TextView>(R.id.tvalamat_pelanggan).text = pelanggan.alamat_pelanggan
        view.findViewById<TextView>(R.id.tvnohp_pelanggan).text = pelanggan.nohp_pelanggan
        view.findViewById<TextView>(R.id.tvcabang_pelanggan).text = pelanggan.cabang
        view.findViewById<TextView>(R.id.tvid_pelanggan).text = pelanggan.id_pelanggan
        val tanggalFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val tanggal = tanggalFormat.format(Date(pelanggan.timestamp ?: 0))
        view.findViewById<TextView>(R.id.terdaftar).text = tanggal
        val btnSunting = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .show()

        btnSunting.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan::class.java).apply {
                putExtra("edit", true)
                putExtra("id", pelanggan.id_pelanggan)
                putExtra("nama", pelanggan.nama_pelanggan)
                putExtra("alamat", pelanggan.alamat_pelanggan)
                putExtra("nohp", pelanggan.nohp_pelanggan)
                putExtra("cabang", pelanggan.cabang)
            }
            this.startActivity(intent)
            dialog.dismiss()
            finish()
        }
        btnHapus.setOnClickListener {
            pelanggan.id_pelanggan?.let { id ->
                konfrimasi(
                    title = getString(R.string.HapusPelanggan),
                    message = "${getString(R.string.KonfirmasiHapus)}, ${pelanggan.nama_pelanggan} ?",
                    onConfirm = {
                        deletePelanggan(id)
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

    // Fungsi hapus pelanggan
    private fun deletePelanggan(pelangganId: String) {
        myRef.child(pelangganId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, data_pelanggan::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, this.getString(R.string.HapusGagal), Toast.LENGTH_SHORT).show()
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
