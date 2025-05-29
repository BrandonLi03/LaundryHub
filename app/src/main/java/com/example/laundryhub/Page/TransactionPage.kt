package com.example.laundryhub.Page

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        transactionRecyclerView = binding.rvTransaction

        setUpRecycler()
    }

    private fun setUpRecycler() {
        val arrayList = databaseHelper.getTransaction(1)
        Log.d("DEBUG", "Total receipt: ${arrayList.size} user: 1")
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