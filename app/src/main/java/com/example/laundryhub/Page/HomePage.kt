package com.example.laundryhub.Page

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laundryhub.Database.DatabaseHelper
import com.example.laundryhub.R

class HomePage : AppCompatActivity() {

    private lateinit var tvNomorAntrian: TextView
    private lateinit var tvAntrianUser: TextView
    private lateinit var btnAmbilAntrian: Button
    private lateinit var tvUser: TextView
    private lateinit var tvQuota: TextView

    private var nomorAntrianSekarang = 1
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 10000
    private val dbHelper = DatabaseHelper(this)

    private var nomorYangDiambilUser: Int? = null

    private val runnable = object : Runnable {
        override fun run() {
            nomorAntrianSekarang++
            tvNomorAntrian.text = "$nomorAntrianSekarang"
            handler.postDelayed(this, updateInterval)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val btnProfile: ImageView = findViewById(R.id.btn_profile)
        val btnNota: ImageView = findViewById(R.id.btn_nota)
        val btnMap: ImageView = findViewById(R.id.btn_map)
        val btnNews: ImageView = findViewById(R.id.btn_news)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)

        tvUser = findViewById(R.id.tv_user)
        tvQuota = findViewById(R.id.tv_quota)
        tvNomorAntrian = findViewById(R.id.tv_nomor_antrian)
        tvAntrianUser = findViewById(R.id.tv_antrian_user)
        btnAmbilAntrian = findViewById(R.id.btn_ambil_antrian)

        if (userId != -1) {
            dbHelper.updateQuotaFromTransactions(userId)
            val user = dbHelper.getUserById(userId)
            if (user != null) {
                tvUser.text = "Welcome, ${user.username}"
                tvQuota.text = "${user.quota} Kg"
                progressBar.max = 105
                progressBar.progress = user.quota
            }
        }

        tvAntrianUser.visibility = View.GONE
        tvNomorAntrian.text = "0"

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        btnNota.setOnClickListener {
            val intent = Intent(this, TransactionPage::class.java)
            startActivity(intent)
        }

        btnMap.setOnClickListener {
            val intent = Intent(this, MapPage::class.java)
            startActivity(intent)
        }

        btnNews.setOnClickListener{
            val intent = Intent(this, NewsPage::class.java)
            startActivity(intent)
        }

        btnAmbilAntrian.setOnClickListener {
            if(nomorYangDiambilUser != null) {
                Toast.makeText(this, "Anda sudah mengambil nomor: $nomorYangDiambilUser", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nomorUntukDiambil = nomorAntrianSekarang

            nomorYangDiambilUser = nomorUntukDiambil

            tvAntrianUser.text = "Nomor Antrian Anda: $nomorYangDiambilUser"
            tvAntrianUser.visibility = View.VISIBLE

            handler.removeCallbacks(runnable)
            nomorAntrianSekarang++
            tvNomorAntrian.text = "$nomorAntrianSekarang"
            handler.postDelayed(runnable, updateInterval)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}