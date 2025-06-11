package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelTambahan
import java.text.NumberFormat
import java.util.Locale

class pilih_tambahan_adapter(
    private val listTambahan: MutableList<ModelTambahan>,
    private val onItemClick: (ModelTambahan) -> Unit
) : RecyclerView.Adapter<pilih_tambahan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTambahan[position]
        holder.tvNama.text = item.nama_tambahan ?: "Tidak Ada Nama"
        holder.tvHarga.text = "Rp %,d".format(item.harga_tambahan ?: 0).replace(',', '.')

        holder.cvCARD.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = listTambahan.size

    fun updateList(newList: List<ModelTambahan>) {
        listTambahan.clear()
        listTambahan.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvnama_tambahan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvharga_tambahan)
        val cvCARD: CardView = itemView.findViewById(R.id.card_data_tambahan)
    }
}
