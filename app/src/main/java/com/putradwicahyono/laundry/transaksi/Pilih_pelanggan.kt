package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.pilih_pelanggan_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.model_data.ModelPelanggan

class Pilih_pelanggan : AppCompatActivity() {

    private lateinit var backarrow: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: pilih_pelanggan_adapter

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")
    private val listPelanggan = arrayListOf<ModelPelanggan>()
    private val listPelangganFull = arrayListOf<ModelPelanggan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_pilih_pelanggan)

        initViews()
        setupRecyclerView()
        setupSearch()
        loadDataFirebase()
        setupBackButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pilihpelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        backarrow = findViewById(R.id.backarrow)
        recyclerView = findViewById(R.id.rvpilihpelanggan)
        searchView = findViewById(R.id.searchpelanggan)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = pilih_pelanggan_adapter(listPelanggan) { selectedPelanggan ->
            val intent = Intent().apply {
                putExtra("idPelanggan", selectedPelanggan.id_pelanggan)
                putExtra("nama", selectedPelanggan.nama_pelanggan)
                putExtra("nohp", selectedPelanggan.nohp_pelanggan)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterPelanggan(newText)
                return true
            }
        })
    }

    private fun filterPelanggan(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            listPelangganFull
        } else {
            val lower = query.lowercase()
            listPelangganFull.filter {
                it.nama_pelanggan?.lowercase()?.contains(lower) == true ||
                        it.alamat_pelanggan?.lowercase()?.contains(lower) == true ||
                        it.nohp_pelanggan?.lowercase()?.contains(lower) == true
            }
        }
        adapter.updateList(filteredList)
    }

    private fun loadDataFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPelanggan.clear()
                listPelangganFull.clear()
                for (data in snapshot.children) {
                    val pelanggan = data.getValue(ModelPelanggan::class.java)
                    pelanggan?.let {
                        listPelanggan.add(it)
                        listPelangganFull.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Bisa tambahkan Toast atau Log di sini kalau mau
            }
        })
    }

    private fun setupBackButton() {
        backarrow.setOnClickListener {
            startActivity(Intent(this, transaksi::class.java))
            finish()
        }
    }
}
