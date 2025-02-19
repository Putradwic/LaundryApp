package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan


class data_pelanggan_adapter(private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<data_pelanggan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvID.text = item.id_pelanggan
        holder.tvNama.text = item.nama_pelanggan
        holder.tvAlamat.text = item.alamat_pelanggan
        holder.tvNoHP.text = item.nohp_pelanggan
        holder.tvTerdaftar.text = item.terdaftar
        holder.tvcabang.text = item.cabang
        holder.cvCARD.setOnClickListener(){
        }
        holder.btHubungi.setOnClickListener{
        }
        holder.btLihat.setOnClickListener{
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_pelanggan)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_pelanggan)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_pelanggan)
        val tvAlamat = itemView.findViewById<TextView>(R.id.tvalamat_pelanggan)
        val tvNoHP = itemView.findViewById<TextView>(R.id.tvnohp_pelanggan)
        val tvTerdaftar = itemView.findViewById<TextView>(R.id.tvterdaftar_pelanggan)
        val btHubungi = itemView.findViewById<Button>(R.id.btn_hubungi)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val tvcabang = itemView.findViewById<TextView>(R.id.tvcabang_pelanggan)
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }
}