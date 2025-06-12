package com.putradwicahyono.laundry.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

    // Declare variables to store intent data
    private var namaPelanggan: String = ""
    private var noHp: String = ""
    private var namaLayanan: String = ""
    private var hargaLayanan: Int = 0
    private var listTambahan: ArrayList<ModelTambahan> = arrayListOf()
    private var metodePembayaran: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_invoice)

        initViews()
        applyWindowInsets()
        displayInvoiceDetails()
        setupClickListeners() // Add this line that was missing
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
        // Generate a simple invoice ID
        val invoiceId = "INV-" + System.currentTimeMillis().toString().substring(8)
        tvInvoiceId.text = "Invoice ID: $invoiceId"

        // Get current date and time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        tvInvoiceDate.text = "Date: ${dateFormat.format(Date())}"

        // Retrieve data from intent and store in class variables
        namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "-"
        noHp = intent.getStringExtra("noHp") ?: "-"
        namaLayanan = intent.getStringExtra("namaLayanan") ?: "-"
        hargaLayanan = intent.getIntExtra("hargaLayanan", 0)
        listTambahan = intent.getSerializableExtra("listTambahan") as? ArrayList<ModelTambahan> ?: arrayListOf()
        metodePembayaran = intent.getStringExtra("metodePembayaran") ?: "Belum dipilih"

        tvCustomerName.text = "${getString(R.string.Nama)} : $namaPelanggan"
        tvCustomerPhone.text = "${getString(R.string.NoHP)}: $noHp"
        tvMainService.text = "${getString(R.string.LayananUtama)}: $namaLayanan"
        tvMainServicePrice.text = "${getString(R.string.Harga)}: ${formatRupiah(hargaLayanan)}"

        // Calculate total price
        var totalPrice = hargaLayanan
        listTambahan.forEach {
            totalPrice += it.harga_tambahan ?: 0
        }

        // Setup RecyclerView for additional services
        adapterAdditional = konfirmasi_tambahan_adapter(listTambahan)
        rvAdditionalServices.layoutManager = LinearLayoutManager(this)
        rvAdditionalServices.adapter = adapterAdditional

        tvTotalPrice.text = "Total: ${formatRupiah(totalPrice)}"
    }

    private fun formatRupiah(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatter.format(amount)
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.btn_whatsapp).setOnClickListener {
            sendInvoiceToWhatsApp()
        }
    }

    private fun sendInvoiceToWhatsApp() {
        val invoiceText = createInvoiceText()

        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_TEXT, invoiceText)

                // Fixed variable name from noHP to noHp
                if (noHp.isNotEmpty()) {
                    val phoneNumber = formatPhoneNumber(noHp)
                    putExtra("jid", "$phoneNumber@s.whatsapp.net")
                }
            }

            startActivity(intent)

        } catch (e: Exception) {
            // Jika WhatsApp tidak terinstall, buka di aplikasi lain
            shareInvoiceToOtherApps(invoiceText)
        }
    }

    private fun createInvoiceText(): String {
        val currentDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        var invoiceText = """
        🧾 *LaundryIn*
        ═══════════════════════
        
        📅 Tanggal: $currentDate
        👤 Nama: $namaPelanggan
        📱 No HP: $noHp
        
        🧺 *LAYANAN*
        • $namaLayanan - Rp ${formatCurrency(hargaLayanan)}
        
    """.trimIndent()

        // Tambah layanan tambahan jika ada
        if (listTambahan.isNotEmpty()) {
            invoiceText += "\n🛍️ *LAYANAN TAMBAHAN*\n"
            var totalTambahan = 0

            for (tambahan in listTambahan) {
                // Fixed property name to match ModelTambahan
                val namaTambahan = tambahan.nama_tambahan ?: "Unknown"
                val hargaTambahan = tambahan.harga_tambahan ?: 0
                invoiceText += "• $namaTambahan - Rp ${formatCurrency(hargaTambahan)}\n"
                totalTambahan += hargaTambahan
            }

            val totalKeseluruhan = hargaLayanan + totalTambahan

            invoiceText += """
            
            ═══════════════════════
            💰 *TOTAL PEMBAYARAN*
            
            *TOTAL: Rp ${formatCurrency(totalKeseluruhan)}*
            
            💳 Metode: $metodePembayaran
            
            ═══════════════════════
            Terima kasih telah menggunakan layanan kami! 🙏
            
        """.trimIndent()

        } else {
            invoiceText += """
            ═══════════════════════
            💰 *TOTAL: Rp ${formatCurrency(hargaLayanan)}*
            💳 Metode: $metodePembayaran
            
            ═══════════════════════
            Terima kasih telah menggunakan layanan kami! 🙏
            
        """.trimIndent()
        }

        return invoiceText
    }

    private fun formatPhoneNumber(phone: String): String {
        var formattedPhone = phone.replace("[^0-9]".toRegex(), "")

        // Ubah format nomor Indonesia
        when {
            formattedPhone.startsWith("08") -> formattedPhone = "62${formattedPhone.substring(1)}"
            formattedPhone.startsWith("8") -> formattedPhone = "62$formattedPhone"
            !formattedPhone.startsWith("62") -> formattedPhone = "62$formattedPhone"
        }

        return formattedPhone
    }

    private fun formatCurrency(amount: Int): String {
        return NumberFormat.getNumberInstance(Locale("id", "ID")).format(amount)
    }

    private fun shareInvoiceToOtherApps(invoiceText: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, invoiceText)
            putExtra(Intent.EXTRA_SUBJECT, "Invoice Laundry - $namaPelanggan")
        }

        startActivity(Intent.createChooser(intent, "Bagikan Invoice"))
    }
}