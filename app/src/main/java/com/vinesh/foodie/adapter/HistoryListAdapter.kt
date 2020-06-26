package com.vinesh.foodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinesh.foodie.R
import com.vinesh.foodie.model.FoodItem
import org.json.JSONArray

class HistoryListAdapter(val context: Context, val itemList: ArrayList<FoodItem>): RecyclerView.Adapter<HistoryListAdapter.historyListViewHolder>() {

    class historyListViewHolder(view: View): RecyclerView.ViewHolder(view){

        val foodItemName: TextView = view.findViewById(R.id.txtHistListFoodItemName)
        val foodItemCost: TextView = view.findViewById(R.id.txtHistListFoodItemPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list_recycler_single_row,parent,false)

        return historyListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: historyListViewHolder, position: Int) {
        val foodItem = itemList[position]

        holder.foodItemName.text = foodItem.itemName
        holder.foodItemCost.text = foodItem.itemCostForOne

    }

}