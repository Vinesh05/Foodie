package com.vinesh.foodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinesh.foodie.R
import com.vinesh.foodie.model.FoodItem
import com.vinesh.foodie.model.Order

class HistoryOrderAdapter(val context: Context, private val order: ArrayList<Order>): RecyclerView.Adapter<HistoryOrderAdapter.historyOrderViewHolder>() {

    class historyOrderViewHolder(view: View):RecyclerView.ViewHolder(view){

        val orderRestaurantName: TextView = view.findViewById(R.id.txtHistResName)
        val orderDate: TextView = view.findViewById(R.id.txtHistDate)
        val orderRecyler: RecyclerView = view.findViewById(R.id.recyclerHistList)
        val orderTotal: TextView = view.findViewById(R.id.txtHistTotalPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_recycler_single_row,parent,false)

        return historyOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return order.size
    }

    override fun onBindViewHolder(holder: historyOrderViewHolder, position: Int) {
        val currentOrder = order[position]

        holder.orderRestaurantName.text = currentOrder.orderResName
        holder.orderDate.text = currentOrder.orderDate
        holder.orderTotal.text = currentOrder.orderCost

        val orderArray = currentOrder.orderArray
        val orderList = ArrayList<FoodItem>()

        for(i in 0 until orderArray.length()){
            val orderListObject = orderArray.getJSONObject(i)
            orderList.add(
                FoodItem(
                    orderListObject.getString("food_item_id"),
                    orderListObject.getString("name"),
                    orderListObject.getString("cost")
                )
            )
        }

        holder.orderRecyler.adapter = HistoryListAdapter(context,orderList)
        holder.orderRecyler.layoutManager = LinearLayoutManager(context)

    }

}