package com.putradwicahyono.laundry.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPegawai
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class data_pegawai_adapter(private val listPegawai: ArrayList<ModelPegawai>,
                             private val onClick: (ModelPegawai) -> Unit) :
    RecyclerView.Adapter<data_pegawai_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPegawai[position]
        holder.tvID.text = item.id_pegawai
        holder.tvNama.text = item.nama_pegawai
        holder.tvAlamat.text = item.alamat_pegawai
        holder.tvNoHP.text = item.nohp_pegawai
        holder.tvcabang.text = item.cabang
        holder.status.text = item.status
        val formatTanggal = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val terdaftar = formatTanggal.format(Date(item.timestamp))
        holder.terdaftar.text = "$terdaftar"

        holder.cvCARD.setOnLongClickListener{
            onClick(item)
            true
        }
        holder.btHubungi.setOnClickListener{
            val nohp = item.nohp_pegawai ?: ""
            val context = holder.itemView.context
            val waIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/62${nohp.trimStart('0')}")
            }
            context.startActivity(waIntent)
        }
        holder.btLihat.setOnClickListener{
            onClick(item)
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD = itemView.findViewById<CardView>(R.id.card_data_pegawai)
        val tvID = itemView.findViewById<TextView>(R.id.tvid_pegawai)
        val tvNama = itemView.findViewById<TextView>(R.id.tvnama_pegawai)
        val tvAlamat = itemView.findViewById<TextView>(R.id.tvalamat_pegawai)
        val tvNoHP = itemView.findViewById<TextView>(R.id.tvnohp_pegawai)
        val terdaftar = itemView.findViewById<TextView>(R.id.terdaftar)
        val btHubungi = itemView.findViewById<Button>(R.id.btn_hubungi)
        val btLihat = itemView.findViewById<Button>(R.id.btn_lihat)
        val tvcabang = itemView.findViewById<TextView>(R.id.tvcabang_pegawai)
        val status = itemView.findViewById<TextView>(R.id.status)
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

}