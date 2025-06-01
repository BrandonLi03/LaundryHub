package com.example.laundryhub.Page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.laundryhub.R

class HomePage : AppCompatActivity() {

    private lateinit var tvNomorAntrian: TextView
    private lateinit var tvAntrianUser: TextView
    private lateinit var btnAmbilAntrian: Button

    private var nomorAntrianSekarang = 1
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 10000

    private var nomorYangDiambilUser: Int? = null

    private val runnable = object : Runnable {
        override fun run() {
            nomorAntrianSekarang++
            tvNomorAntrian.text = "$nomorAntrianSekarang"
            handler.postDelayed(this, updateInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val tvUser: TextView = findViewById(R.id.tv_user)
        tvNomorAntrian = findViewById(R.id.tv_nomor_antrian)
        tvAntrianUser = findViewById(R.id.tv_antrian_user)
        btnAmbilAntrian = findViewById(R.id.btn_ambil_antrian)

//        ambil username saat login
//        tvUser.text = "Welcome,  ${user}"

        tvAntrianUser.visibility = View.GONE
        tvNomorAntrian.text = "0"

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