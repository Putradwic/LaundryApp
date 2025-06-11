package com.putradwicahyono.laundry.tambahan

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.data_tambahan_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.layanan.data_layanan
import com.putradwicahyono.laundry.model_data.ModelTambahan

class data_tambahan : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")

    private lateinit var rvdatatambahan: RecyclerView
    private lateinit var fab_tambah_tambahan: FloatingActionButton
    private var listTambahan = arrayListOf<ModelTambahan>()
    private lateinit var backarrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)

        initViews()
        setupRecyclerView()
        setupListeners()
        getData()
        back()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        rvdatatambahan = findViewById(R.id.rvdatatambahan)
        fab_tambah_tambahan = findViewById(R.id.fab_tambah_tambahan)
        backarrow = findViewById(R.id.backarrow)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatatambahan.layoutManager = layoutManager
        rvdatatambahan.setHasFixedSize(true)
    }

    private fun setupListeners() {
        fab_tambah_tambahan.setOnClickListener {
            // Pastikan kelas activity untuk tambah pelanggan sudah terdaftar di AndroidManifest.xml
            val intent = Intent(this, tambah_tambahan::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ambil data dari firebase
    private fun getData() {
        val query = myRef.orderByChild("idTambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTambahan.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val tambahan = dataSnapshot.getValue(ModelTambahan::class.java)
                        tambahan?.let { listTambahan.add(it) }
                    }
                    val adapter = data_tambahan_adapter(listTambahan) { tambahan ->
                        munculDialogtambahan(tambahan)
                    }
                    rvdatatambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            // Penanganan eror
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_tambahan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    // Memunculkan dialog mod berdasarkan layout
    fun munculDialogtambahan(tambahan: ModelTambahan) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_mod_tambahan, null)
        view.findViewById<TextView>(R.id.tvnama_tambahan).text = tambahan.nama_tambahan
        val hargaFormatted = "Rp %,d".format(tambahan.harga_tambahan ?: 0).replace(',', '.')
        view.findViewById<TextView>(R.id.tvharga_tambahan).text = hargaFormatted
        view.findViewById<TextView>(R.id.tvnama_cabang_tambahan).text = tambahan.cabang
        val btnSunting = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .show()

        btnSunting.setOnClickListener {
            val intent = Intent(this, tambah_tambahan::class.java).apply {
                putExtra("edit", true)
                putExtra("idTambahan", tambahan.id_tambahan)
                putExtra("nama", tambahan.nama_tambahan)
                putExtra("harga", tambahan.harga_tambahan.toString())
                putExtra("cabang", tambahan.cabang)
            }
            this.startActivity(intent)
            dialog.dismiss()
            finish()
        }

        btnHapus.setOnClickListener {
            tambahan.id_tambahan?.let { id ->
                konfrimasi(
                    title = getString(R.string.HapusTambahan),
                    message = "${getString(R.string.KonfirmasiHapus4)}, ${tambahan.nama_tambahan} ?",
                    onConfirm = {
                        deleteTambahan(id)
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
    private fun deleteTambahan(tambahanId: String) {
        myRef.child(tambahanId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil4), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, data_tambahan::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, this.getString(R.string.HapusGagal4), Toast.LENGTH_SHORT).show()
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