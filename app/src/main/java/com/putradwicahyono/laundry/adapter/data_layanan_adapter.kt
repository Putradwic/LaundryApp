package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelLayanan


class data_layanan_adapter(private val listLayanan: ArrayList<ModelLayanan>,
                           private val onClick: (ModelLayanan) -> Unit) :
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
        val harga = item.harga_layanan ?: 0
        val formattedPrice = "Rp %,d".format(harga).replace(',', '.')
        holder.tvHarga.text = formattedPrice
        holder.tvcabang.text = item.cabang

        holder.cvCARD.setOnLongClickListener{
            onClick(item)
            true
        }
        holder.btLihat.setOnClickListener{
            onClick(item)
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_layanan)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_layanan)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_layanan)
        val tvHarga = itemView.findViewById<TextView>(R.id.tvharga_layanan)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val tvcabang = itemView.findViewById<TextView>(R.id.tvnama_cabang_layanan)
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

}