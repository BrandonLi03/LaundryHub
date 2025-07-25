package com.example.laundryhub.Page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundryhub.Adapter.TransactionAdapter
import com.example.laundryhub.Database.DatabaseHelper
import com.example.laundryhub.Model.Transaction
import com.example.laundryhub.databinding.ActivityTransactionPageBinding

class TransactionPage : AppCompatActivity(), TransactionAdapter.OnItemClickListener {

    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: ActivityTransactionPageBinding
    private lateinit var back_btn: ImageButton
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        transactionRecyclerView = binding.rvTransaction
        back_btn = binding.btnBack

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("userId", -1)

        setUpRecycler()

        back_btn.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecycler() {
        val arrayList = databaseHelper.getTransaction(userId)
        Log.d("DEBUG", "Total receipt: ${arrayList.size} user: ${userId}")
        transactionAdapter = TransactionAdapter(arrayList, this)
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionRecyclerView.adapter = transactionAdapter
    }

    override fun onItemClick(transaction: Transaction) {
        Log.d("TransactionPage", "Clicked: ${transaction.receiptCode}")
        val intent = Intent(this, DetailPage::class.java)
        intent.putExtra("receipCode", transaction.receiptCode)
        intent.putExtra("dropDate", transaction.dropDate)
        intent.putExtra("pickUpDate", transaction.pickUpDate)
        startActivity(intent)
    }
}