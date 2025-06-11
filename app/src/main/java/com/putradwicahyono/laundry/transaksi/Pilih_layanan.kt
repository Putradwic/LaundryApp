package com.putradwicahyono.laundry.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.pilih_layanan_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.model_data.ModelLayanan

class Pilih_layanan : AppCompatActivity() {

    private lateinit var backarrow: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: pilih_layanan_adapter

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("layanan")
    private val listLayanan = arrayListOf<ModelLayanan>()
    private val listLayananFull = arrayListOf<ModelLayanan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_layanan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pilihlayanan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupSearch()
        loadDataFirebase()
        setupBackButton()
    }

    private fun initViews() {
        backarrow = findViewById(R.id.backarrow)
        recyclerView = findViewById(R.id.rvpilihlayanan)
        searchView = findViewById(R.id.searchlayanan)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = pilih_layanan_adapter(listLayanan) { selectedLayanan ->
            val intent = Intent().apply {
                putExtra("idLayanan", selectedLayanan.id_layanan)
                putExtra("nama", selectedLayanan.nama_layanan)
                putExtra("harga", selectedLayanan.harga_layanan)
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
                filterLayanan(newText)
                return true
            }
        })
    }

    private fun filterLayanan(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            listLayananFull
        } else {
            val lower = query.lowercase()
            listLayananFull.filter {
                it.nama_layanan?.lowercase()?.contains(lower) == true
            }
        }
        adapter.updateList(filteredList)
    }

    private fun loadDataFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listLayanan.clear()
                listLayananFull.clear()
                for (data in snapshot.children) {
                    val layanan = data.getValue(ModelLayanan::class.java)
                    layanan?.let {
                        listLayanan.add(it)
                        listLayananFull.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

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
