package com.putradwicahyono.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.pilih_pelanggan_adapter
import com.putradwicahyono.laundry.model_data.ModelPelanggan

class pilih_pelanggan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: pilih_pelanggan_adapter

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
    private val listPelanggan = arrayListOf<ModelPelanggan>()
    private val listPelangganFull = arrayListOf<ModelPelanggan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pelanggan)

        recyclerView = findViewById(R.id.rvpilihpelanggan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = pilih_pelanggan_adapter(listPelanggan) { selectedPelanggan ->
            val intent = Intent()
            intent.putExtra("idPelanggan", selectedPelanggan.id_pelanggan)
            intent.putExtra("nama", selectedPelanggan.nama_pelanggan)
            intent.putExtra("nohp", selectedPelanggan.nohp_pelanggan)
            setResult(RESULT_OK, intent)
            finish()
        }
        recyclerView.adapter = adapter

        // Ambil data dari Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPelanggan.clear()
                listPelangganFull.clear()
                for (data in snapshot.children) {
                    val pelanggan = data.getValue(ModelPelanggan::class.java)
                    if (pelanggan != null) {
                        listPelanggan.add(pelanggan)
                        listPelangganFull.add(pelanggan)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: Tambahkan error handling
            }

        })
    }
}
