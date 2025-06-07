package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelCabang


class data_cabang_adapter(private val listCabang: ArrayList<ModelCabang>,
                          private val onClick: (ModelCabang) -> Unit) :
    RecyclerView.Adapter<data_cabang_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCabang[position]
        holder.tvID.text = item.id_cabang
        holder.tvNama.text = item.nama_cabang
        holder.tvalamat.text = item.alamat_cabang
        holder.tvnohp.text = item.nohp_cabang

        holder.cvCARD.setOnLongClickListener{
            onClick(item)
            true
        }
        holder.btLihat.setOnClickListener{
            onClick(item)
            true
        }
        holder.btHubungi.setOnClickListener{
            onClick(item)
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_cabang)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_cabang)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_cabang)
        val tvalamat = itemView.findViewById<TextView>(R.id.tvalamat_cabang)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val btHubungi = itemView.findViewById<Button>(R.id.btn_hubungi)
        val tvnohp = itemView.findViewById<TextView>(R.id.tvnohp_cabang)
    }

    override fun getItemCount(): Int {
        return listCabang.size
    }

}