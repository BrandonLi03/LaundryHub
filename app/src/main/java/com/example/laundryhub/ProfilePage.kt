package com.example.laundryhub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundryhub.Database.DatabaseHelper

class ProfilePage : AppCompatActivity() {

    private lateinit var ivProfile: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvUsername: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivProfile = findViewById(R.id.ivProfile)
        tvUsername = findViewById(R.id.tvUsername)
        tvPhone = findViewById(R.id.tvPhone)
        btnLogout = findViewById(R.id.btnRegist)
        dbHelper = DatabaseHelper(this)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)

        if (userId != -1) {
            val user = dbHelper.getUserById(userId)
            if (user != null) {
                tvUsername.text = user.username
                tvPhone.text = user.phoneNumber.toString()
            }
        }

        ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnLogout.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            ivProfile.setImageURI(imageUri)
        }
    }
}
