package com.putradwicahyono.laundry.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import com.putradwicahyono.laundry.transaksi.transaksi

class pilih_pelanggan_adapter(
    private val listPelanggan: MutableList<ModelPelanggan>,private val onItemClick: (ModelPelanggan) -> Unit
) : RecyclerView.Adapter<pilih_pelanggan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvNama.text = item.nama_pelanggan ?: "Tidak Ada Nama"
        holder.tvAlamat.text = item.alamat_pelanggan ?: "Tidak Ada Alamat"
        holder.tvNoHP.text = item.nohp_pelanggan ?: "Tidak Ada No HP"

        holder.cvCARD.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = listPelanggan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvnama_pelanggan)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvalamat_pelanggan)
        val tvNoHP: TextView = itemView.findViewById(R.id.tvnohp_pelanggan)
        val cvCARD: CardView = itemView.findViewById(R.id.card_data_pelanggan)
    }

    fun updateList(newList: List<ModelPelanggan>) {
        listPelanggan.clear()
        listPelanggan.addAll(newList)
        notifyDataSetChanged()
    }
}
