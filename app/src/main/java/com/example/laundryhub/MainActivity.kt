package com.example.laundryhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundryhub.Page.HomePage
import com.example.laundryhub.Page.TransactionPage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var btn = findViewById<Button>(R.id.btn)
        var btnHome = findViewById<Button>(R.id.btn_home)

        btn.setOnClickListener(){
            val intent = Intent(this, TransactionPage::class.java)
            startActivity(intent)
        }

        btnHome.setOnClickListener(){
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }
}