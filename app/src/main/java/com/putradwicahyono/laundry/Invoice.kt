package com.putradwicahyono.laundry.transaksi

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.putradwicahyono.laundry.R
import com.putradwicahyono.laundry.adapter.konfirmasi_tambahan_adapter
import com.putradwicahyono.laundry.model_data.ModelTambahan
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Invoice : AppCompatActivity() {

    private lateinit var tvInvoiceId: TextView
    private lateinit var tvInvoiceDate: TextView
    private lateinit var tvCustomerName: TextView
    private lateinit var tvCustomerPhone: TextView
    private lateinit var tvMainService: TextView
    private lateinit var tvMainServicePrice: TextView
    private lateinit var rvAdditionalServices: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var adapterAdditional: konfirmasi_tambahan_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_invoice) // We will create this layout next

        initViews()
        applyWindowInsets()
        displayInvoiceDetails()
    }

    private fun initViews() {
        tvInvoiceId = findViewById(R.id.tv_invoice_id)
        tvInvoiceDate = findViewById(R.id.tv_invoice_date)
        tvCustomerName = findViewById(R.id.tv_customer_name)
        tvCustomerPhone = findViewById(R.id.tv_customer_phone)
        tvMainService = findViewById(R.id.tv_main_service_name)
        tvMainServicePrice = findViewById(R.id.tv_main_service_price)
        rvAdditionalServices = findViewById(R.id.rv_additional_services)
        tvTotalPrice = findViewById(R.id.tv_total_price)
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Invoice)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun displayInvoiceDetails() {
        // Generate a simple invoice ID (you might want a more robust solution)
        val invoiceId = "INV-" + System.currentTimeMillis().toString().substring(8)
        tvInvoiceId.text = "Invoice ID: $invoiceId"

        // Get current date and time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        tvInvoiceDate.text = "Date: ${dateFormat.format(Date())}"

        // Retrieve data from intent
        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "-"
        val noHp = intent.getStringExtra("noHp") ?: "-"
        val namaLayanan = intent.getStringExtra("namaLayanan") ?: "-"
        val hargaLayanan = intent.getIntExtra("hargaLayanan", 0)
        val listTambahan = intent.getSerializableExtra("listTambahan") as? ArrayList<ModelTambahan> ?: arrayListOf()

        tvCustomerName.text = "Customer: $namaPelanggan"
        tvCustomerPhone.text = "Phone: $noHp"
        tvMainService.text = "Service: $namaLayanan"
        tvMainServicePrice.text = "Price: ${formatRupiah(hargaLayanan)}"

        // Calculate total price
        var totalPrice = hargaLayanan
        listTambahan.forEach {
            totalPrice += it.harga_tambahan ?: 0
        }

        // Setup RecyclerView for additional services
        adapterAdditional = konfirmasi_tambahan_adapter(listTambahan) { /* No delete action in invoice */ }
        rvAdditionalServices.layoutManager = LinearLayoutManager(this)
        rvAdditionalServices.adapter = adapterAdditional

        tvTotalPrice.text = "Total: ${formatRupiah(totalPrice)}"
    }

    private fun formatRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatter.format(amount)
    }
}