package com.putradwicahyono.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelPegawai
import com.putradwicahyono.laundry.pegawai.tambah_pegawai
import java.lang.ref.Reference

class data_pegawai_adapter(private val listPegawai: ArrayList<ModelPegawai>) :
    RecyclerView.Adapter<data_pegawai_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        return ViewHolder(view)
    }

    lateinit var appContext:Context
    lateinit var databasbaseReference: DatabaseReference
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPegawai[position]
        appContext = holder.itemView.context
        holder.tvID.text = item.id_pegawai
        holder.tvNama.text = item.nama_pegawai
        holder.tvNoHP.text = item.nohp_pegawai
        holder.tvAlamat.text = item.alamat_pegawai
        holder.tvcabang.text = item.cabang


        holder.cvCARD.setOnClickListener {
            val intent = Intent(appContext, tambah_pegawai::class.java)
            intent.putExtra("judul", "Edit Pegawai")
            intent.putExtra("idPegawai", item.id_pegawai)
            intent.putExtra("namaPegawai", item.nama_pegawai)
            intent.putExtra("noHPPegawai", item.nohp_pegawai)
            intent.putExtra("alamatPegawai",item.alamat_pegawai)
            intent.putExtra("cabang", item.cabang)
            appContext.startActivity(intent)
        }
        holder.btHubungi.setOnClickListener {
            // aksi ketika tombol hubungi ditekan
        }
        holder.btLihat.setOnClickListener {
            // aksi ketika tombol lihat ditekan
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD: CardView = itemView.findViewById(R.id.card_data_pegawai)
        val tvID: TextView = itemView.findViewById(R.id.tvid_pegawai)
        val tvNama: TextView = itemView.findViewById(R.id.tvnama_pegawai)
        val tvNoHP: TextView = itemView.findViewById(R.id.tvnohp_pegawai)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvalamat_pegawai)
        val btHubungi: Button = itemView.findViewById(R.id.btn_hubungi)
        val btLihat: Button = itemView.findViewById(R.id.btn_lihat)
        val tvcabang: TextView = itemView.findViewById(R.id.tvcabang_pegawai)
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }


}
