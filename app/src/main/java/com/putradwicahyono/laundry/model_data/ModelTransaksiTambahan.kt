package com.example.laundry.modeldata

import java.io.Serializable

class ModelTransaksiTambahan(
    val idLayanan: String? = "",
    val namaLayanan: String? = "",
    val hargaLayanan: String? = "",
    val tanggalTerdaftar: String? = ""
) : Serializable