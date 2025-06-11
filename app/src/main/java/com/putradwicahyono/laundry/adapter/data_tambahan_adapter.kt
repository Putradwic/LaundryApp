package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelTambahan
import java.text.NumberFormat
import java.util.Locale


class data_tambahan_adapter(private val listTambahan: ArrayList<ModelTambahan>,
                            private val onClick: (ModelTambahan) -> Unit) :
    RecyclerView.Adapter<data_tambahan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTambahan[position]
        holder.tvID.text = item.id_tambahan
        holder.tvNama.text = item.nama_tambahan
        holder.tvHarga.text = "Rp %,d".format(item.harga_tambahan ?: 0).replace(',', '.')
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
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_tambahan)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_tambahan)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_tambahan)
        val tvHarga = itemView.findViewById<TextView>(R.id.tvharga_tambahan)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val tvcabang = itemView.findViewById<TextView>(R.id.tvnama_cabang_tambahan)
    }

    override fun getItemCount(): Int {
        return listTambahan.size
    }

}