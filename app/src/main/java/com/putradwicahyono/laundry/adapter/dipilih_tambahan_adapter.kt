package com.putradwicahyono.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.model_data.ModelTambahan

class dipilih_tambahan_adapter(
    private val listTambahanDipilih: MutableList<ModelTambahan>,
    private val onDelete: (ModelTambahan) -> Unit
) : RecyclerView.Adapter<dipilih_tambahan_adapter.TambahanViewHolder>() {

    inner class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama = itemView.findViewById<TextView>(R.id.tvnama_tambahan)
        val harga = itemView.findViewById<TextView>(R.id.tvharga_tambahan)
        val btnHapus = itemView.findViewById<ImageView>(R.id.btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_dipilih_tambahan, parent, false)
        return TambahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
        val item = listTambahanDipilih[position]
        holder.nama.text = item.nama_tambahan ?: "Tidak ada nama"
        holder.harga.text = "Rp %,d".format(item.harga_tambahan ?: 0).replace(',', '.')

        holder.btnHapus.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = listTambahanDipilih.size
}