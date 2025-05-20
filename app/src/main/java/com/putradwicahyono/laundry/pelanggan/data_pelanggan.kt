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
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.data_pelanggan_adapter
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_pelanggan : AppCompatActivity() {

    // Inisialisasi Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    private lateinit var rvdatapelanggan: RecyclerView
    private lateinit var fab_tambah_pelanggan: FloatingActionButton
    private var listPelanggan = arrayListOf<ModelPelanggan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelanggan)
        enableEdgeToEdge()

        initViews()
        setupRecyclerView()
        setupListeners()
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        rvdatapelanggan = findViewById(R.id.rvdatapelanggan)
        fab_tambah_pelanggan = findViewById(R.id.fab_tambah_pelanggan)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdatapelanggan.layoutManager = layoutManager
        rvdatapelanggan.setHasFixedSize(true)
    }

    private fun setupListeners() {
        fab_tambah_pelanggan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getData() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPelanggan.clear()
                if (snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        pelanggan?.let { listPelanggan.add(it) }
                    }
                    val adapter = data_pelanggan_adapter(listPelanggan)
                    rvdatapelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_pelanggan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
