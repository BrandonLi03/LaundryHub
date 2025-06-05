package com.example.laundryhub.Page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundryhub.Adapter.TransactionAdapter
import com.example.laundryhub.Adapter.TransactionItemsAdapter
import com.example.laundryhub.Database.DatabaseHelper
import com.example.laundryhub.R
import com.example.laundryhub.databinding.ActivityDetailPageBinding

class DetailPage : AppCompatActivity() {

    private lateinit var detailRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var detailAdapter: TransactionItemsAdapter
    private lateinit var binding: ActivityDetailPageBinding
    private lateinit var receiptCodes: TextView
    private lateinit var DropDate: TextView
    private lateinit var pickUpDate: TextView
    private lateinit var back_btn: ImageButton
    var receiptCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        detailRecyclerView = binding.rvDetail

        receiptCode = intent.getIntExtra("receipCode", 0)
        receiptCodes = binding.tvDetailReceiptCode
        DropDate = binding.tvDetailDropDate
        pickUpDate = binding.tvDetailPickupDate
        back_btn = binding.btnBack

        receiptCodes.text = receiptCode.toString()
        DropDate.text = intent.getStringExtra("dropDate")
        pickUpDate.text = intent.getStringExtra("pickUpDate")

        setUpRecycler()

        back_btn.setOnClickListener{
            val intent = Intent(this, TransactionPage::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecycler() {
        val itemList = databaseHelper.getItems(receiptCode)
        Log.d("DEBUG", "Total receipt: ${itemList.size} receipt code: ${receiptCode}")
        detailAdapter = TransactionItemsAdapter(itemList)
        detailRecyclerView.layoutManager = LinearLayoutManager(this)
        detailRecyclerView.adapter = detailAdapter
    }
}