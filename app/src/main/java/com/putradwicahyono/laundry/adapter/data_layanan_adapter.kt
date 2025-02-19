package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelLayanan

class data_layanan_adapter(private val listLayanan: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<data_layanan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.tvID.text = item.id_layanan
        holder.tvNama.text = item.nama_layanan
        holder.tvcabang.text = item.cabang
        holder.tvharga.text = item.harga_layanan

        holder.cvCARD.setOnClickListener {
            // aksi ketika CardView ditekan
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD: CardView = itemView.findViewById(R.id.card_data_layanan)
        val tvID: TextView = itemView.findViewById(R.id.tvid_layanan)
        val tvNama: TextView = itemView.findViewById(R.id.tvnama_layanan)
        val tvharga: TextView = itemView.findViewById(R.id.tvharga_layanan)
        val tvcabang: TextView = itemView.findViewById(R.id.tvnama_cabang_layanan)
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }
}
