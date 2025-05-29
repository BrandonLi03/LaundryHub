package com.example.laundryhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundryhub.Model.Transaction
import com.example.laundryhub.R

class TransactionAdapter(private val transactionList: ArrayList<Transaction>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(transaction: Transaction)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var receiptCode = itemView.findViewById<TextView>(R.id.tv_receipt_code)
        private var date = itemView.findViewById<TextView>(R.id.tv_date)
        private var cardView = itemView.findViewById<CardView>(R.id.cv_transaction)

        fun bind(transaction: Transaction, clickListener: OnItemClickListener){
            receiptCode.text =transaction.receiptCode.toString()
            date.text = transaction.dropDate
            cardView.setOnClickListener {
                clickListener.onItemClick(transaction)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.transaction_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position], itemClickListener)
    }
}