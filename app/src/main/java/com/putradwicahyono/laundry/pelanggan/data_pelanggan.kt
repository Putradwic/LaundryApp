package com.putradwicahyono.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.putradwicahyono.laundry.adapter.data_pelanggan_adapter
import com.putradwicahyono.laundry.model_data.ModelPelanggan

class data_pelanggan : AppCompatActivity() {

    // Inisialisasi Firebase Realtime Database
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    // Inisialisasi Komponen UI
    private lateinit var rvdatapelanggan: RecyclerView
    private lateinit var pelangganList: ArrayList<ModelPelanggan>
    private lateinit var fab_tambah_pelanggan: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        // Inisialisasi View
        init()

        // Setup RecyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatapelanggan.layoutManager = layoutManager
        rvdatapelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf()

        // Memuat Data dari Firebase
        getData()

        // Set OnClickListener untuk FAB
        fab_tambah_pelanggan.setOnClickListener {
            Toast.makeText(this, "FAB ditekan", Toast.LENGTH_SHORT).show()
            // Aksi saat FAB ditekan, misalnya pindah ke halaman tambah pelanggan
            startActivity(Intent(this, tambah_pelanggan::class.java))
        }

        // Mengatasi masalah WindowInsets (Jika FAB masih tidak bisa ditekan, hapus blok ini)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Inisialisasi Komponen UI
    private fun init() {
        fab_tambah_pelanggan = findViewById(R.id.fab_tambah_pelanggan)
        rvdatapelanggan = findViewById(R.id.rvdatapelanggan)
    }

    // Fungsi untuk Memuat Data dari Firebase
    private fun getData() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pengguna = dataSnapshot.getValue(ModelPelanggan::class.java)
                        if (pengguna != null) {
                            pelangganList.add(pengguna)
                        }
                    }
                    // Mengatur Adapter untuk RecyclerView
                    val adapter = data_pelanggan_adapter(pelangganList)
                    rvdatapelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Jika terjadi error
                Toast.makeText(this@data_pelanggan, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
