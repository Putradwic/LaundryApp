package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class data_pelanggan_adapter(private val listPelanggan: ArrayList<ModelPelanggan>,
                             private val onClick: (ModelPelanggan) -> Unit) :
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
        holder.tvcabang.text = item.cabang
        val formatTanggal = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val terdaftar = formatTanggal.format(Date(item.timestamp))
        holder.terdaftar.text = "$terdaftar"

        holder.cvCARD.setOnLongClickListener{
            onClick(item)
            true
        }
        holder.btHubungi.setOnClickListener{
        }
        holder.btLihat.setOnClickListener{
            onClick(item)
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_pelanggan)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_pelanggan)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_pelanggan)
        val tvAlamat = itemView.findViewById<TextView>(R.id.tvalamat_pelanggan)
        val tvNoHP = itemView.findViewById<TextView>(R.id.tvnohp_pelanggan)
        val terdaftar = itemView.findViewById<TextView>(R.id.terdaftar)
        val btHubungi = itemView.findViewById<Button>(R.id.btn_hubungi)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val tvcabang = itemView.findViewById<TextView>(R.id.tvcabang_pelanggan)
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

}