package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelTambahan
import java.text.NumberFormat
import java.util.*

class konfirmasi_tambahan_adapter(
    private val listTambahan: MutableList<ModelTambahan>,
    private val onHapusClick: (ModelTambahan) -> Unit
) : RecyclerView.Adapter<konfirmasi_tambahan_adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvnama_tambahan)
        val harga: TextView = view.findViewById(R.id.tvharga_tambahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_konfirmasi_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTambahan[position]
        holder.nama.text = item.nama_tambahan
        holder.harga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(item.harga_tambahan)
    }

    override fun getItemCount(): Int = listTambahan.size
}
