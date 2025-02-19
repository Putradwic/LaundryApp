package com.putradwicahyono.laundry.pegawai

import android.annotation.SuppressLint
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
import com.putradwicahyono.laundry.adapter.data_pegawai_adapter
import com.putradwicahyono.laundry.model_data.ModelPegawai
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_pegawai : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")

    private lateinit var rvdata_pegawai: RecyclerView
    private lateinit var fab_tambah_pegawai: FloatingActionButton
    private var listPegawai = arrayListOf<ModelPegawai>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        initViews()
        setupRecyclerView()
        setupListeners()
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pegawai)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        rvdata_pegawai = findViewById(R.id.rvdatapegawai)
        fab_tambah_pegawai = findViewById(R.id.fab_tambah_pegawai)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvdata_pegawai.layoutManager = layoutManager
        rvdata_pegawai.setHasFixedSize(true)
    }

    private fun setupListeners() {
        fab_tambah_pegawai.setOnClickListener {
            // Pastikan kelas activity untuk tambah pelanggan sudah terdaftar di AndroidManifest.xml
            val intent = Intent(this, tambah_pegawai::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        // Ambil data berdasarkan idPelanggan dan batasi 100 data terakhir
        val query = myRef.orderByChild("idPegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPegawai.clear()
                if (snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPegawai::class.java)
                        pelanggan?.let { listPegawai.add(it) }
                    }
                    // Perbarui adapter RecyclerView
                    val adapter = data_pegawai_adapter(listPegawai)
                    rvdata_pegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_pegawai, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}