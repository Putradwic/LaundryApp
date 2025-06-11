package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.pilih_tambahan_adapter
import com.putradwicahyono.laundry.laundry
import com.putradwicahyono.laundry.model_data.ModelTambahan

class Pilih_tambahan : AppCompatActivity() {

    private lateinit var backarrow: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: pilih_tambahan_adapter

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("tambahan")
    private val listTambahan = arrayListOf<ModelTambahan>()
    private val listTambahanFull = arrayListOf<ModelTambahan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_tambahan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pilihtambahan)) { v, insets ->
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
        recyclerView = findViewById(R.id.rvpilihtambahan)
        searchView = findViewById(R.id.searchlayanan)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = pilih_tambahan_adapter(listTambahan) { selectedTambahan ->
            val intent = Intent().apply {
                putExtra("idTambahan", selectedTambahan.id_tambahan)
                putExtra("nama", selectedTambahan.nama_tambahan)
                putExtra("harga", selectedTambahan.harga_tambahan)
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
                filterTambahan(newText)
                return true
            }
        })
    }

    private fun filterTambahan(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            listTambahanFull
        } else {
            val lower = query.lowercase()
            listTambahanFull.filter {
                it.nama_tambahan?.lowercase()?.contains(lower) == true
            }
        }
        adapter.updateList(filteredList)
    }

    private fun loadDataFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTambahan.clear()
                listTambahanFull.clear()
                for (data in snapshot.children) {
                    val tambahan = data.getValue(ModelTambahan::class.java)
                    tambahan?.let {
                        listTambahan.add(it)
                        listTambahanFull.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Pilih_tambahan, "Gagal ambil data: ${error.message}", Toast.LENGTH_SHORT).show()
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
