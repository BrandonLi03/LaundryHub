package com.example.laundryhub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundryhub.Model.DetailTransaction
import com.example.laundryhub.R

class TransactionItemsAdapter(private val itemList: ArrayList<DetailTransaction>) : RecyclerView.Adapter<TransactionItemsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var clothing = itemView.findViewById<TextView>(R.id.tv_clothing)
        private var quantity = itemView.findViewById<TextView>(R.id.tv_quantity)

        fun bind(item: DetailTransaction) {
            clothing.text = item.clothing
            quantity.text = "${item.quantity} pcs"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}