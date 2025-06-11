package com.putradwicahyono.laundry.model_data

import java.io.Serializable

class ModelTambahan (
    val  id_tambahan: String? = null,
    val  nama_tambahan: String? = null,
    val  harga_tambahan: Int? = 0,
    val  cabang: String? = null,
): Serializable
