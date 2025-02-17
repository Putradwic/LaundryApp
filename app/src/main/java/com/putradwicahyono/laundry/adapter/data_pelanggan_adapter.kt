package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPelanggan


class data_pelanggan_adapter(
    private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<data_pelanggan_adapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int):
            ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvid_pelanggan.text = item.id_pelanggan
        holder.tvnama_pelanggan.text = item.nama_pelanggan
        holder.tvalamat_pelanggan.text = item.alamat_pelanggan
        holder.tvnohp_pelanggan.text = item.nohp_pelanggan
        holder.tvterdaftar_pelanggan.text = item.terdaftar
        holder.card_data_pelanggan.setOnClickListener{

        }
        holder.btn_lihat.setOnClickListener{

        }
        holder.btn_hubungi.setOnClickListener{

        }

    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvid_pelanggan: TextView = itemView.findViewById(R.id.tvid_pelanggan)
        val tvnama_pelanggan: TextView = itemView.findViewById(R.id.tvnama_pelanggan)
        val tvalamat_pelanggan: TextView = itemView.findViewById(R.id.tvalamat_pelanggan)
        val tvnohp_pelanggan: TextView = itemView.findViewById(R.id.tvnohp_pelanggan)
        val card_data_pelanggan: CardView = itemView.findViewById(R.id.card_data_pelanggan)
        val tvterdaftar_pelanggan: TextView = itemView.findViewById(R.id.tvterdaftar_pelanggan)
        val btn_lihat: TextView = itemView.findViewById(R.id.btn_lihat)
        val btn_hubungi: TextView = itemView.findViewById(R.id.btn_hubungi)
    }
}
