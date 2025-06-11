package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelLayanan
import java.text.NumberFormat
import java.util.Locale

class pilih_layanan_adapter(
    private val listLayanan: MutableList<ModelLayanan>,
    private val onItemClick: (ModelLayanan) -> Unit
) : RecyclerView.Adapter<pilih_layanan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.tvNama.text = item.nama_layanan ?: "Tidak Ada Nama"
        holder.tvHarga.text = "Rp %,d".format(item.harga_layanan ?: 0).replace(',', '.')

        holder.cvCARD.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = listLayanan.size

    fun updateList(newList: List<ModelLayanan>) {
        listLayanan.clear()
        listLayanan.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvnama_layanan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvharga_layanan)
        val cvCARD: CardView = itemView.findViewById(R.id.card_data_layanan)
    }
}
