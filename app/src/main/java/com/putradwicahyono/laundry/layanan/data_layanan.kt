package com.putradwicahyono.laundry.layanan



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
import com.putradwicahyono.laundry.adapter.data_layanan_adapter
import com.putradwicahyono.laundry.model_data.ModelLayanan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putradwicahyono.laundry.laundry


class data_layanan : AppCompatActivity() {

    // Inisialisasi Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")

    private lateinit var rvdatalayanan: RecyclerView
    private lateinit var fab_tambah_layanan: FloatingActionButton
    private var listLayanan = arrayListOf<ModelLayanan>()
    private lateinit var backarrow: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_layanan)

        // Enable edge-to-edge untuk tampilan modern
        enableEdgeToEdge()

        initViews()
        setupRecyclerView()
        setupListeners()
        getData()
        back()

        // Atur padding agar tidak terhalang oleh system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        rvdatalayanan = findViewById(R.id.rvdatalayanan)
        fab_tambah_layanan = findViewById(R.id.fab_tambah_layanan)
        backarrow = findViewById(R.id.backarrow)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatalayanan.layoutManager = layoutManager
        rvdatalayanan.setHasFixedSize(true)
    }

    private fun setupListeners() {
        fab_tambah_layanan.setOnClickListener {
            // Pastikan kelas activity untuk tambah pelanggan sudah terdaftar di AndroidManifest.xml
            val intent = Intent(this, tambah_layanan::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ambil data dari firebase
    private fun getData() {
        val query = myRef.orderByChild("idLayanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listLayanan.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(ModelLayanan::class.java)
                        layanan?.let { listLayanan.add(it) }
                    }
                    val adapter = data_layanan_adapter(listLayanan) { layanan ->
                        munculDialogLayanan(layanan)
                    }
                    rvdatalayanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            // Penanganan eror
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_layanan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    // Memunculkan dialog mod berdasarkan layout
    fun munculDialogLayanan(layanan: ModelLayanan) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_mod_layanan, null)
        view.findViewById<TextView>(R.id.tvnama_layanan).text = layanan.nama_layanan
        val hargaFormatted = "Rp %,d".format(layanan.harga_layanan ?: 0).replace(',', '.')
        view.findViewById<TextView>(R.id.tvharga_layanan).text = hargaFormatted
        view.findViewById<TextView>(R.id.tvnama_cabang_layanan).text = layanan.cabang
        val btnSunting = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .show()

        btnSunting.setOnClickListener {
            val intent = Intent(this, tambah_layanan::class.java).apply {
                putExtra("edit", true)
                putExtra("idLayanan", layanan.id_layanan)
                putExtra("nama", layanan.nama_layanan)
                putExtra("harga", layanan.harga_layanan.toString())
                putExtra("cabang", layanan.cabang)
            }
            this.startActivity(intent)
            dialog.dismiss()
            finish()
        }

        btnHapus.setOnClickListener {
            layanan.id_layanan?.let { id ->
                konfrimasi(
                    title = getString(R.string.HapusLayanan),
                    message = "${getString(R.string.KonfirmasiHapus2)}, ${layanan.nama_layanan} ?",
                    onConfirm = {
                        deleteLayanan(id)
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
    private fun deleteLayanan(layananId: String) {
        myRef.child(layananId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil2), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, data_layanan::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, this.getString(R.string.HapusBerhasil2), Toast.LENGTH_SHORT).show()
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
